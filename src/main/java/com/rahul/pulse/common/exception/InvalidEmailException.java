package com.rahul.pulse.common.exception;

public class InvalidEmailException extends DomainException {
    public InvalidEmailException() { super("Invalid Email");}
    public InvalidEmailException(String message) {
        super(message);
    }
}
