package io.iron.ironworker.client;

public class APIException extends Exception {
    private final String message;
    private final Exception innerException;
    
    public APIException(String message, Exception innerException) {
        this.message = message;
        this.innerException = innerException;
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
