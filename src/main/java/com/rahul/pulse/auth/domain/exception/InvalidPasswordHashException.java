package com.rahul.pulse.auth.domain.exception;

import com.rahul.pulse.common.exception.DomainException;

public class InvalidPasswordHashException extends DomainException {
    public InvalidPasswordHashException(String message) {
        super(message);
    }
    public InvalidPasswordHashException() {
        super("Invalid Password Hash");
    }
}
