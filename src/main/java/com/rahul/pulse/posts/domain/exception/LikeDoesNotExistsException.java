package com.rahul.pulse.posts.domain.exception;

import com.rahul.pulse.common.exception.DomainException;

public class LikeDoesNotExistsException extends DomainException {
    public LikeDoesNotExistsException() { super("Like does not exists");}
    public LikeDoesNotExistsException(String message) {
        super(message);
    }
}
