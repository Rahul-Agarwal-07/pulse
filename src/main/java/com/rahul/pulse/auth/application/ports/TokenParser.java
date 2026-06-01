package com.rahul.pulse.auth.application.ports;

public interface TokenParser {

    String extractUserId(String token);
    boolean isValid(String token);
    boolean isRefreshToken(String token);

}
