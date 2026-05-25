package com.rahul.pulse.auth.domain.exception;

import com.rahul.pulse.common.exception.DomainException;

public class UserAlreadyExistsException extends DomainException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
    public UserAlreadyExistsException() {
        super("User Already Exists");
    }
}
