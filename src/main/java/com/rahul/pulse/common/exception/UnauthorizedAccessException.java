package com.rahul.pulse.common.exception;

public class UnauthorizedAccessException extends RuntimeException {
    public UnauthorizedAccessException(){ super("Unauthorized Access");}
    public UnauthorizedAccessException(String message) {
        super(message);
    }
}
