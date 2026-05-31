package com.rahul.pulse.auth.application.usecase;

public interface TokenParser {

    String extractUserId(String token);
    boolean isValid(String token);

}
