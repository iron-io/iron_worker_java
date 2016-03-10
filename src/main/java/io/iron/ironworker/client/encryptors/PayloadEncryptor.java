package io.iron.ironworker.client.encryptors;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.GCMParameterSpec;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

public class PayloadEncryptor {

    // AES-GCM parameters
    private static final int AES_KEY_SIZE = 128; // in bits
    private static final int GCM_NONCE_LENGTH = 12; // in bytes
    private static final int GCM_TAG_LENGTH = 16; // in bytes (overhead)
    
    private static final String PEM_FILE_EXTENSION = "pem";
    private static final String DER_FILE_EXTENSION = "der";

    private PublicKey rsaPublicKey;
    private Key aesKey;
    private SecureRandom secureRandom;

    public PayloadEncryptor(String encryptionKey) throws GeneralSecurityException, IOException {
        init();
        rsaPublicKey = getPublicKey(encryptionKey);
    }
    
    public PayloadEncryptor(File encryptionKeyFile) throws GeneralSecurityException, IOException {
        init();
        rsaPublicKey = getPublicKey(encryptionKeyFile);
    }

    public String encrypt(String payload) throws GeneralSecurityException {
        byte[] aesKeyBytes = aesKey.getEncoded();
        byte[] aesKeyCipher = encryptRSA(aesKeyBytes, rsaPublicKey);

        Cipher gcm = getAesCipher(aesKey);
        byte[] pbytes = payload.getBytes();
        byte[] cipherPayload = encrypt(pbytes, gcm);

        byte[] ciphertext = new byte[pbytes.length + GCM_TAG_LENGTH + GCM_NONCE_LENGTH];
        System.arraycopy(gcm.getIV(), 0, ciphertext, ciphertext.length-GCM_NONCE_LENGTH, GCM_NONCE_LENGTH);
        System.arraycopy(cipherPayload, 0, ciphertext, 0, cipherPayload.length);
        return new String(Base64.encode(ArrayUtils.addAll(aesKeyCipher, ciphertext)));
    }
    
    private void init() throws GeneralSecurityException{
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        secureRandom = new SecureRandom();
        aesKey = generateAESKey();
    }
    
    private PublicKey getPublicKey(String encryptionKey) throws IOException, GeneralSecurityException{
        PemReader pemReader = new PemReader(new StringReader(encryptionKey));
        PemObject pemObject = pemReader.readPemObject();
        pemReader.close();
        byte[] keyBytes = pemObject.getContent();
        return generatePublicKeyFromBytes(keyBytes);
    }
    
    private PublicKey getPublicKey(File encryptionKeyFile) throws GeneralSecurityException, IOException {
        byte[] keyBytes;
        PublicKey publicKey;
        String keyFileExtension = FilenameUtils.getExtension(encryptionKeyFile.getName());
        if(keyFileExtension.equals(PEM_FILE_EXTENSION)){
            String publicKeyString = FileUtils.readFileToString(encryptionKeyFile);
            publicKey = getPublicKey(publicKeyString);
        } else if (keyFileExtension.equals(DER_FILE_EXTENSION)){
            keyBytes = parseDerFile(encryptionKeyFile);
            publicKey = generatePublicKeyFromBytes(keyBytes);
        } else {
            throw new UnsupportedKeyFileFormatException("Unsupported key file format: "+ keyFileExtension);
        }

        return publicKey;
    }    

    private byte[] parseDerFile(File derFile) throws IOException {
        FileInputStream fis = new FileInputStream(derFile);
        byte[] keyBytes = new byte[(int) derFile.length()];
        DataInputStream dis = new DataInputStream(fis);
        dis.readFully(keyBytes);
        dis.close();
        return keyBytes;
    }
    
    private PublicKey generatePublicKeyFromBytes(byte[] keyBytes) throws GeneralSecurityException {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance(Algorithm.RSA);
        return kf.generatePublic(spec);
    }

    private Key generateAESKey() throws GeneralSecurityException {
        KeyGenerator generator = KeyGenerator.getInstance(Algorithm.AES);
        generator.init(AES_KEY_SIZE);
        Key aesKey = generator.generateKey();
        return aesKey;
    }

    private byte[] encryptRSA(byte[] input, Key publicKey) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(Algorithm.OAEP_WITH_SHA1);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey, secureRandom);
        return encrypt(input, cipher);
    }

    private byte[] encrypt(byte[] input, Cipher cipher) throws GeneralSecurityException {
        byte[] cipherText;
        cipherText = cipher.doFinal(input);
        return cipherText;
    }

    private Cipher getAesCipher(Key aesKey) throws GeneralSecurityException {
        byte[] nonce = new byte[GCM_NONCE_LENGTH];
        Cipher cipher = Cipher.getInstance(Algorithm.AES_GCM);        
        secureRandom.nextBytes(nonce);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, nonce);
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, spec);
        return cipher;
    }
}