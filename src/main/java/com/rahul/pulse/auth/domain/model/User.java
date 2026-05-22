package com.rahul.pulse.auth.domain.model;

import java.time.Instant;

public class User {

    private UserId id;

    private Email email;

    private PasswordHash passwordHash;

    private String fullName;

    private Instant createdAt;

    public User(
            UserId id,
            Email email,
            PasswordHash passwordHash,
            String fullName,
            Instant createdAt
    ) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.createdAt = createdAt;
    }

}