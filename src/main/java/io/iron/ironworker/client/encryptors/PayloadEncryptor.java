package io.iron.ironworker.client.encryptors;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

import org.apache.commons.lang3.ArrayUtils;
import org.bouncycastle.util.encoders.Base64;

public class PayloadEncryptor {

    // AES-GCM parameters
    private static final int AES_KEY_SIZE = 128; // in bits
    private static final int GCM_NONCE_LENGTH = 12; // in bytes
    private static final int GCM_TAG_LENGTH = 16; // in bytes (overhead)

    private final PublicKey rsaPublicKey;
    private final Key aesKey;
    private final SecureRandom secureRandom;

    public PayloadEncryptor(String encryptionKeyFile) throws GeneralSecurityException, IOException {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        secureRandom = new SecureRandom();
        rsaPublicKey = getPublicKey(encryptionKeyFile);
        aesKey = generateAESKey();
    }

    public String encryptPayload(String payload) throws GeneralSecurityException {
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

    private Key generateAESKey() throws GeneralSecurityException {
        KeyGenerator generator;
        generator = KeyGenerator.getInstance("AES", "SunJCE");
        generator.init(AES_KEY_SIZE);
        Key keyToBeWrapped = generator.generateKey();
        return keyToBeWrapped;
    }

    private byte[] encryptRSA(byte[] input, Key publicKey) throws GeneralSecurityException {
        Cipher cipher;
        cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey, secureRandom);
        return encrypt(input, cipher);
    }

    private byte[] encrypt(byte[] input, Cipher cipher) throws GeneralSecurityException {
        byte[] cipherText;
        cipherText = cipher.doFinal(input);
        return cipherText;
    }

    private Cipher getAesCipher(Key aesKey) throws GeneralSecurityException {
        Cipher cipher;
        byte[] nonce = new byte[GCM_NONCE_LENGTH];
        cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
        secureRandom.nextBytes(nonce);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, nonce);
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, spec);
        
        return cipher;
    }

    private PublicKey getPublicKey(String filename) throws GeneralSecurityException, IOException {
        File f = new File(filename);
        FileInputStream fis;
        fis = new FileInputStream(f);
        DataInputStream dis = new DataInputStream(fis);
        byte[] keyBytes = new byte[(int) f.length()];
        dis.readFully(keyBytes);
        dis.close();
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);        
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);        
    }
}