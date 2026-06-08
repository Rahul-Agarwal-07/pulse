package com.rahul.pulse.common.exception;

public class ResourceNotFoundException extends DomainException {
    public ResourceNotFoundException() { super("Resource Not Found");}
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
