package com.rahul.pulse.auth.domain.model;

import java.time.Instant;
import java.util.UUID;

public class RefreshToken {

    private RefreshTokenId id;

    private UserId userId;

    private String tokenHash;

    private Instant expiresAt;

    private boolean revoked;

    private Instant createdAt;

    public RefreshToken(
            UserId userId,
            String tokenHash,
            Instant expiresAt,
            boolean revoked,
            Instant createdAt
    ) {
        this.id = new RefreshTokenId(UUID.randomUUID());
        this.userId = userId;
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
        this.revoked = revoked;
        this.createdAt = createdAt;
    }

    public RefreshTokenId getId() {
        return id;
    }

    public UserId getUserId() {
        return userId;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}