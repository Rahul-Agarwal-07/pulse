package com.rahul.pulse.auth.domain.exception;

import com.rahul.pulse.common.exception.DomainException;

public class RefreshTokenReuseDetectedException extends DomainException {
    public RefreshTokenReuseDetectedException() { super("Refresh Token Reuse Detected");}
    public RefreshTokenReuseDetectedException(String message) {
        super(message);
    }
}
