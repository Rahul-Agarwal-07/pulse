package com.rahul.pulse.auth.infrastructure;

import com.rahul.pulse.auth.domain.model.RefreshToken;
import com.rahul.pulse.auth.domain.model.UserId;
import com.rahul.pulse.auth.domain.repository.RefreshTokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Transactional
public class RefreshTokenRepositoryAdapterIT {

    @ServiceConnection
    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17")
                    .withDatabaseName("pulse_test")
                    .withUsername("postgres")
                    .withPassword("postgres");

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Test
    void should_save_refresh_token()
    {
        Instant now = Instant.now();

        RefreshToken refreshToken = new RefreshToken(
                new UserId(UUID.randomUUID()),
                "hashed-token",
                now.plus(7, ChronoUnit.DAYS),
                false,
                now
        );

        refreshTokenRepository.save(refreshToken);

        RefreshToken token = refreshTokenRepository.findByTokenHash(refreshToken.getTokenHash())
                .orElse(null);

        assertNotNull(token);
    }

    @Test
    void should_find_refresh_token_by_hash()
    {
        Instant now = Instant.now();

        RefreshToken refreshToken = new RefreshToken(
                new UserId(UUID.randomUUID()),
                "hashed-token",
                now.plus(7, ChronoUnit.DAYS),
                false,
                now
        );

        refreshTokenRepository.save(refreshToken);

        RefreshToken token = refreshTokenRepository.findByTokenHash(refreshToken.getTokenHash())
                .orElseThrow();

        assertNotNull(token);
        assertEquals(refreshToken.getUserId(), token.getUserId());
        assertEquals(refreshToken.getExpiresAt(), token.getExpiresAt());
        assertEquals(refreshToken.getCreatedAt(), token.getCreatedAt());
        assertEquals(refreshToken.getTokenHash(), token.getTokenHash());
    }

    @Test
    void should_revoke_refresh_token()
    {
        Instant now = Instant.now();

        RefreshToken refreshToken = new RefreshToken(
                new UserId(UUID.randomUUID()),
                "hashed-token",
                now.plus(7, ChronoUnit.DAYS),
                false,
                now
        );

        refreshTokenRepository.save(refreshToken);

        int updated = refreshTokenRepository.revoke(refreshToken.getId());

        RefreshToken stored =
                refreshTokenRepository.findByTokenHash(refreshToken.getTokenHash())
                        .orElseThrow();

        assertEquals(1, updated);
        assertTrue(stored.isRevoked());
    }

    @Test
    void should_revoke_all_refresh_tokens()
    {
        Instant now = Instant.now();
        UserId userId =  new UserId(UUID.randomUUID());

        RefreshToken refreshToken1 = new RefreshToken(
                userId,
                "hashed-token1",
                now.plus(7, ChronoUnit.DAYS),
                false,
                now
        );

        refreshTokenRepository.save(refreshToken1);

        RefreshToken refreshToken2 = new RefreshToken(
                userId,
                "hashed-token2",
                now.plus(7, ChronoUnit.DAYS),
                false,
                now
        );

        refreshTokenRepository.save(refreshToken2);

        int updated = refreshTokenRepository.revokeAllByUserId(userId);

        RefreshToken token1 =
                refreshTokenRepository.findByTokenHash("hashed-token1")
                        .orElseThrow();

        RefreshToken token2 =
                refreshTokenRepository.findByTokenHash("hashed-token2")
                        .orElseThrow();

        assertTrue(token1.isRevoked());
        assertTrue(token2.isRevoked());

        assertEquals(2, updated);


    }

}
