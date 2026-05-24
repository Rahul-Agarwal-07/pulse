package com.rahul.pulse.auth.application.ports;

public interface PasswordHasher {
    String encode(String password);
    boolean matches(String rawPassword, String passwordHash);
}
