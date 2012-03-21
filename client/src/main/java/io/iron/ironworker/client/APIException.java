package io.iron.ironworker.client;

public class APIException extends Exception {
    private String message;
    
    public APIException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String toString() {
        return message;
    }
}
