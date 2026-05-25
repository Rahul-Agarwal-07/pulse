package com.rahul.pulse.auth.domain.exception;

import com.rahul.pulse.common.exception.DomainException;

public class InvalidEmailException extends DomainException {
    public InvalidEmailException() { super("Invalid Email");}
    public InvalidEmailException(String message) {
        super(message);
    }
}
