package com.rahul.pulse.auth.infrastructure.security;

import com.rahul.pulse.auth.application.ports.PasswordHasher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordHasher implements PasswordHasher {

    private final BCryptPasswordEncoder hasher = new BCryptPasswordEncoder();

    @Override
    public String encode(String password) {
        return hasher.encode(password);
    }

    @Override
    public boolean matches(String rawPassword, String passwordHash) {
        return hasher.matches(rawPassword, passwordHash);
    }


}
