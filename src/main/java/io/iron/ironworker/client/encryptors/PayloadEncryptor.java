package io.iron.ironworker.client.encryptors;

import io.iron.ironworker.client.APIException;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.GCMParameterSpec;

import org.apache.commons.lang.*;
import org.bouncycastle.util.encoders.Base64;

public class PayloadEncryptor {
	
	// AES-GCM parameters
    private static final int AES_KEY_SIZE = 128; // in bits
    private static final int GCM_NONCE_LENGTH = 12; // in bytes
    private static final int GCM_TAG_LENGTH = 16; // in bytes (overhead)
    
    private PublicKey rsaPublicKey;
    private Key aesKey;
    private SecureRandom secureRandom;
    
    public PayloadEncryptor(String encryptionKeyFile) throws APIException {
    	Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    	secureRandom = new SecureRandom();
    	rsaPublicKey = getPublicKey(encryptionKeyFile);
    	aesKey = generateAESKey();
    }
    
    public String encryptPayload(String payload) throws APIException {
    	byte[] aesKeyBytes = aesKey.getEncoded();
		byte[] aesKeyCipher = encryptRSA(aesKeyBytes, rsaPublicKey);
		
        Cipher gcm = getAesCipher(aesKey);
		byte[] pbytes = payload.getBytes();
		byte[] cipherPayload = encrypt(pbytes, gcm);
		
		byte[] ciphertext = new byte[pbytes.length + GCM_TAG_LENGTH + GCM_NONCE_LENGTH];
		System.arraycopy(gcm.getIV(),0,ciphertext,ciphertext.length - GCM_NONCE_LENGTH, GCM_NONCE_LENGTH);
		System.arraycopy(cipherPayload,0,ciphertext,0,cipherPayload.length);
		return new String(Base64.encode(ArrayUtils.addAll(aesKeyCipher, ciphertext)));
    }
    
    private Key generateAESKey() throws APIException {
	    KeyGenerator generator;
		try {
			generator = KeyGenerator.getInstance("AES", "SunJCE");
		} catch (Exception e) {
			throw new APIException(e.getMessage(), e);
		}
	    generator.init(AES_KEY_SIZE);
	    Key keyToBeWrapped = generator.generateKey();
	    return keyToBeWrapped;
	}
    
    private byte[] encryptRSA(byte[] input, Key publicKey) throws APIException {
    	Cipher cipher;
	    try {
	    	cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey, secureRandom);
		} catch (Exception e) {
			throw new APIException(e.getMessage(), e);
		}	    
	    return encrypt(input, cipher);
	}
    
    private byte[] encrypt(byte[] input, Cipher cipher) throws APIException {
    	byte[] cipherText;
		try {
			cipherText = cipher.doFinal(input);
		} catch (Exception e) {
			throw new APIException(e.getMessage(), e);
		}
    	return cipherText;
    }
    
    private Cipher getAesCipher(Key aesKey) throws APIException {
   		Cipher cipher;
   		byte[] nonce = new byte[GCM_NONCE_LENGTH];
		try {
			cipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");
		} catch (Exception e) {
			throw new APIException(e.getMessage(), e);
		}
        secureRandom.nextBytes(nonce);
        GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, nonce);
        try {
			cipher.init(Cipher.ENCRYPT_MODE, aesKey, spec);
		} catch (Exception e) {
			throw new APIException(e.getMessage(), e);
		}
        return cipher;
   	}
    
    private PublicKey getPublicKey(String filename) throws APIException {
	    File f = new File(filename);
	    FileInputStream fis;
		try {
			fis = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			throw new APIException(String.format("File %s not found", filename), e);
		}
	    DataInputStream dis = new DataInputStream(fis);
	    byte[] keyBytes = new byte[(int)f.length()];
	    try {
			dis.readFully(keyBytes);
		    dis.close();
	    } catch (IOException e) {
			throw new APIException(e.getMessage(), e);
		}

	    X509EncodedKeySpec spec =
	      new X509EncodedKeySpec(keyBytes);	    
		try {
			KeyFactory kf = KeyFactory.getInstance("RSA");
			return kf.generatePublic(spec);
		} catch (Exception e) {
			throw new APIException(e.getMessage(), e);
		}
    }
}