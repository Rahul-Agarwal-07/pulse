package com.rahul.pulse.common.exception;

public class InvalidPasswordHashException extends DomainException {
    public InvalidPasswordHashException(String message) {
        super(message);
    }
    public InvalidPasswordHashException() {
        super("Invalid Password Hash");
    }
}
