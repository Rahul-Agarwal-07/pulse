package com.rahul.pulse.auth.application.usecase;

import com.rahul.pulse.auth.application.dto.RefreshTokenCommand;
import com.rahul.pulse.auth.application.dto.RefreshTokenResult;
import com.rahul.pulse.auth.application.ports.RefreshTokenUseCase;
import com.rahul.pulse.auth.application.ports.TokenGenerator;
import com.rahul.pulse.auth.application.ports.TokenHasher;
import com.rahul.pulse.auth.application.ports.TokenParser;
import com.rahul.pulse.auth.domain.exception.InvalidTokenException;
import com.rahul.pulse.auth.domain.exception.RefreshTokenReuseDetectedException;
import com.rahul.pulse.auth.domain.model.RefreshToken;
import com.rahul.pulse.auth.domain.model.User;
import com.rahul.pulse.auth.domain.model.UserId;
import com.rahul.pulse.auth.domain.repository.RefreshTokenRepository;
import com.rahul.pulse.auth.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RefreshTokenUseCaseImplTest {


    private TokenGenerator tokenGenerator;
    private TokenHasher tokenHasher;
    private TokenParser tokenParser;

    private RefreshTokenRepository refreshTokenRepository;
    private UserRepository userRepository;

    private RefreshTokenUseCase refreshTokenUseCase;

    @BeforeEach
    void setup()
    {
        tokenGenerator = mock(TokenGenerator.class);
        tokenHasher = mock(TokenHasher.class);
        tokenParser = mock(TokenParser.class);

        refreshTokenRepository = mock(RefreshTokenRepository.class);
        userRepository = mock(UserRepository.class);

        refreshTokenUseCase = new RefreshTokenUseCaseImpl(
                tokenGenerator,
                tokenParser,
                tokenHasher,
                refreshTokenRepository,
                userRepository
        );
    }

    @Test
    void should_rotate_refresh_token_successfully()
    {
        RefreshTokenCommand command =  new RefreshTokenCommand(
                "refresh-token"
        );

        Instant now = Instant.now();

        User user = mock(User.class);

        RefreshToken token =  new RefreshToken(
                new UserId(UUID.randomUUID()),
                "hashed-token",
                now.plus(7, ChronoUnit.DAYS),
                false,
                now
        );

        when(tokenParser.isValid(command.refreshToken()))
                .thenReturn(true);

        when(tokenParser.isRefreshToken(command.refreshToken()))
                .thenReturn(true);

        when(tokenHasher.hash(command.refreshToken()))
                .thenReturn(token.getTokenHash());

        when(refreshTokenRepository.findByTokenHash(token.getTokenHash()))
                .thenReturn(Optional.of(token));

        when(userRepository.findById(token.getUserId()))
                .thenReturn(Optional.of(user));

        when(tokenGenerator.generateRefreshToken(user))
                .thenReturn("new-refresh-token");

        when(tokenHasher.hash("new-refresh-token"))
                .thenReturn("new-hashed-refresh-token");

        when(refreshTokenRepository.revoke(token.getId()))
                .thenReturn(1);

        when(tokenGenerator.generateAccessToken(user))
                .thenReturn("access-token");

        RefreshTokenResult result = refreshTokenUseCase.refresh(command);

        assertNotNull(result);
        assertEquals("access-token", result.accessToken());
        assertEquals("new-refresh-token", result.refreshToken());

        verify(refreshTokenRepository).save(any(RefreshToken.class));
        verify(refreshTokenRepository).revoke(token.getId());
        verify(refreshTokenRepository).save(any(RefreshToken.class));
        verify(tokenGenerator).generateAccessToken(user);
        verify(tokenGenerator).generateRefreshToken(user);

        ArgumentCaptor<RefreshToken> captor =
                ArgumentCaptor.forClass(RefreshToken.class);

        verify(refreshTokenRepository).save(captor.capture());
        RefreshToken saved = captor.getValue();

        assertNotNull(saved);
        assertEquals(token.getUserId(), saved.getUserId());
        assertEquals("new-hashed-refresh-token", saved.getTokenHash());
    }

    @Test
    void should_throw_when_token_is_invalid()
    {
        RefreshTokenCommand command =  new RefreshTokenCommand(
                "invalid-refresh-token"
        );

        when(tokenParser.isValid(command.refreshToken()))
                .thenReturn(false);

        assertThrows(
                InvalidTokenException.class,
                () -> refreshTokenUseCase.refresh(command)
        );

        verify(refreshTokenRepository, never()).save(any(RefreshToken.class));
    }

    @Test
    void should_throw_when_token_is_not_refresh_token()
    {
        RefreshTokenCommand command =  new RefreshTokenCommand(
                "invalid-token"
        );

        when(tokenParser.isValid(command.refreshToken()))
                .thenReturn(true);

        when(tokenParser.isRefreshToken(command.refreshToken()))
                .thenReturn(false);

        assertThrows(
                InvalidTokenException.class,
                () -> refreshTokenUseCase.refresh(command)
        );

        verify(refreshTokenRepository, never()).save(any(RefreshToken.class));
    }

    @Test
    void should_throw_when_token_does_not_exist()
    {
        RefreshTokenCommand command =  new RefreshTokenCommand(
                "invalid-token"
        );

        when(tokenParser.isValid(command.refreshToken()))
                .thenReturn(true);

        when(tokenParser.isRefreshToken(command.refreshToken()))
                .thenReturn(true);

        when(tokenHasher.hash(command.refreshToken()))
                .thenReturn("token-hash");

        when(refreshTokenRepository.findByTokenHash("token-hash"))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidTokenException.class,
                () -> refreshTokenUseCase.refresh(command)
        );

        verify(refreshTokenRepository, never()).save(any(RefreshToken.class));
    }

    @Test
    void should_throw_when_token_is_expired()
    {
        RefreshTokenCommand command =  new RefreshTokenCommand(
                "refresh-token"
        );

        Instant now = Instant.now();

        RefreshToken token =  new RefreshToken(
                new UserId(UUID.randomUUID()),
                "hashed-token",
                now,
                false,
                now.minus(7, ChronoUnit.DAYS)
        );

        when(tokenParser.isValid(command.refreshToken()))
                .thenReturn(true);

        when(tokenParser.isRefreshToken(command.refreshToken()))
                .thenReturn(true);

        when(tokenHasher.hash(command.refreshToken()))
                .thenReturn(token.getTokenHash());

        when(refreshTokenRepository.findByTokenHash(token.getTokenHash()))
                .thenReturn(Optional.of(token));

        assertThrows(
                InvalidTokenException.class,
                () -> refreshTokenUseCase.refresh(command)
        );

        verify(refreshTokenRepository, never()).save(any(RefreshToken.class));
    }

    @Test
    void should_revoke_all_when_reuse_detected()
    {
        RefreshTokenCommand command =  new RefreshTokenCommand(
                "refresh-token"
        );

        Instant now = Instant.now();

        RefreshToken token =  new RefreshToken(
                new UserId(UUID.randomUUID()),
                "hashed-token",
                now.plus(7, ChronoUnit.DAYS),
                true,
                now
        );

        when(tokenParser.isValid(command.refreshToken()))
                .thenReturn(true);

        when(tokenParser.isRefreshToken(command.refreshToken()))
                .thenReturn(true);

        when(tokenHasher.hash(command.refreshToken()))
                .thenReturn(token.getTokenHash());

        when(refreshTokenRepository.findByTokenHash(token.getTokenHash()))
                .thenReturn(Optional.of(token));

        assertThrows(
                RefreshTokenReuseDetectedException.class,
                () -> refreshTokenUseCase.refresh(command)
        );

        verify(refreshTokenRepository, never()).save(any(RefreshToken.class));
        verify(refreshTokenRepository).revokeAllByUserId(token.getUserId());
    }

    @Test
    void should_throw_when_user_not_found()
    {
        RefreshTokenCommand command =  new RefreshTokenCommand(
                "refresh-token"
        );

        Instant now = Instant.now();

        RefreshToken token =  new RefreshToken(
                new UserId(UUID.randomUUID()),
                "hashed-token",
                now.plus(7, ChronoUnit.DAYS),
                false,
                now
        );

        when(tokenParser.isValid(command.refreshToken()))
                .thenReturn(true);

        when(tokenParser.isRefreshToken(command.refreshToken()))
                .thenReturn(true);

        when(tokenHasher.hash(command.refreshToken()))
                .thenReturn(token.getTokenHash());

        when(refreshTokenRepository.findByTokenHash(token.getTokenHash()))
                .thenReturn(Optional.of(token));

        when(userRepository.findById(token.getUserId()))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidTokenException.class,
                () -> refreshTokenUseCase.refresh(command)
        );

        verify(refreshTokenRepository, never()).save(any(RefreshToken.class));
    }

    @Test
    void should_throw_when_refresh_token_revoke_fails()
    {
        RefreshTokenCommand command =  new RefreshTokenCommand(
                "refresh-token"
        );

        Instant now = Instant.now();

        RefreshToken token =  new RefreshToken(
                new UserId(UUID.randomUUID()),
                "hashed-token",
                now.plus(7, ChronoUnit.DAYS),
                false,
                now
        );

        when(tokenParser.isValid(command.refreshToken()))
                .thenReturn(true);

        when(tokenParser.isRefreshToken(command.refreshToken()))
                .thenReturn(true);

        when(tokenHasher.hash(command.refreshToken()))
                .thenReturn(token.getTokenHash());

        when(refreshTokenRepository.findByTokenHash(token.getTokenHash()))
                .thenReturn(Optional.of(token));

        when(userRepository.findById(token.getUserId()))
                .thenReturn(Optional.of(mock(User.class)));

        when(refreshTokenRepository.revoke(token.getId()))
                .thenReturn(0);

        assertThrows(
                InvalidTokenException.class,
                () -> refreshTokenUseCase.refresh(command)
        );

        verify(refreshTokenRepository, never()).save(any(RefreshToken.class));
    }

}
