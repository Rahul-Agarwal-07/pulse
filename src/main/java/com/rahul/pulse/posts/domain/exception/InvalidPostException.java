package com.rahul.pulse.posts.domain.exception;

import com.rahul.pulse.common.exception.DomainException;

public class InvalidPostException extends DomainException {
    public InvalidPostException() { super("Invalid Post");}
    public InvalidPostException(String message) {
        super(message);
    }
}
