package io.iron.ironworker.client.encryptors;

import org.bouncycastle.crypto.RuntimeCryptoException;

public class UnsupportedKeyFileFormatException extends RuntimeCryptoException {
    private String message;
    
    public UnsupportedKeyFileFormatException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String toString() {
        return message;
    }
}
