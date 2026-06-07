package com.rahul.pulse.auth.application.usecase;

import com.rahul.pulse.auth.application.dto.LogoutCommand;
import com.rahul.pulse.auth.application.ports.LogoutUseCase;
import com.rahul.pulse.auth.application.ports.TokenHasher;
import com.rahul.pulse.auth.application.ports.TokenParser;
import com.rahul.pulse.auth.domain.exception.InvalidTokenException;
import com.rahul.pulse.auth.domain.model.RefreshToken;
import com.rahul.pulse.auth.domain.model.RefreshTokenId;
import com.rahul.pulse.auth.domain.model.User;
import com.rahul.pulse.auth.domain.model.UserId;
import com.rahul.pulse.auth.domain.repository.RefreshTokenRepository;
import com.rahul.pulse.auth.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class LogoutUseCaseTest {

    private RefreshTokenRepository refreshTokenRepository;
    private UserRepository userRepository;
    private TokenParser parser;
    private TokenHasher hasher;

    private LogoutUseCase useCase;

    @BeforeEach
    void setup()
    {
        refreshTokenRepository = mock(RefreshTokenRepository.class);
        userRepository = mock(UserRepository.class);
        parser = mock(TokenParser.class);
        hasher = mock(TokenHasher.class);

        useCase = new LogoutUseCaseImpl(
                refreshTokenRepository,
                userRepository,
                parser,
                hasher
        );
    }

    @Test
    void should_logout_successfully()
    {
        LogoutCommand command = new LogoutCommand(
                "refresh-token"
        );

        Instant now = Instant.now();

        RefreshToken refreshToken = new RefreshToken(
                new UserId(UUID.randomUUID()),
                "hashed-token",
                now.plus(7, ChronoUnit.DAYS),
                false,
                now
        );

        when(parser.isValid(command.refreshToken()))
                .thenReturn(true);

        when(parser.isRefreshToken(command.refreshToken()))
                .thenReturn(true);

        when(parser.extractUserId(command.refreshToken()))
                .thenReturn(refreshToken.getUserId().value().toString());

        when(userRepository.findById(refreshToken.getUserId()))
                .thenReturn(Optional.of(mock(User.class)));

        when(hasher.hash(command.refreshToken()))
                .thenReturn("hashed-token");

        when(refreshTokenRepository.findByTokenHash("hashed-token"))
                .thenReturn(Optional.of(refreshToken));

        when(refreshTokenRepository.revoke(refreshToken.getId()))
                .thenReturn(1);

        useCase.logout(command);

        verify(refreshTokenRepository).revoke(refreshToken.getId());
    }

    @Test
    void should_throw_when_token_is_invalid()
    {
        LogoutCommand command = new LogoutCommand(
                "refresh-token"
        );

        when(parser.isValid(command.refreshToken()))
                .thenReturn(false);

        assertThrows(
                InvalidTokenException.class,
                () -> useCase.logout(command)
        );

        verify(refreshTokenRepository, never()).revoke(any(RefreshTokenId.class));
    }

    @Test
    void should_throw_when_token_is_not_refresh_token()
    {
        LogoutCommand command = new LogoutCommand(
                "refresh-token"
        );

        when(parser.isValid(command.refreshToken()))
                .thenReturn(true);

        when(parser.isRefreshToken(command.refreshToken()))
                .thenReturn(false);

        assertThrows(
                InvalidTokenException.class,
                () -> useCase.logout(command)
        );

        verify(refreshTokenRepository, never()).revoke(any(RefreshTokenId.class));
    }

}
