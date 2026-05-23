package com.rahul.pulse.common.exception;

public class UserAlreadyExistsException extends DomainException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
    public UserAlreadyExistsException() {
        super("User Already Exists");
    }
}
