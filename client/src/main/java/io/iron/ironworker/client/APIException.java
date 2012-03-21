package io.iron.ironworker.client;

public class APIException extends Exception {
    private String message;
    private Exception innerException;
    
    public APIException(String message, Exception innerException) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Exception getInnerException() {
        return innerException;
    }

    public String toString() {
        return message != null ? message : innerException.toString();
    }
}
