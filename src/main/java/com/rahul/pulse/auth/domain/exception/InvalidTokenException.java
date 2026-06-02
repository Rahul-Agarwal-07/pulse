package com.rahul.pulse.auth.domain.exception;

import com.rahul.pulse.common.exception.DomainException;

public class InvalidTokenException extends DomainException {
    public InvalidTokenException() { super("Invalid Token");}
    public InvalidTokenException(String message) {
        super(message);
    }
}
