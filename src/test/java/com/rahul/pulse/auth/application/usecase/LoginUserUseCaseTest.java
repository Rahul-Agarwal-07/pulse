package com.rahul.pulse.auth.application.usecase;

import com.rahul.pulse.auth.application.dto.LoginUserCommand;
import com.rahul.pulse.auth.application.dto.LoginUserResult;
import com.rahul.pulse.auth.application.ports.LoginUserUseCase;
import com.rahul.pulse.auth.application.ports.PasswordHasher;
import com.rahul.pulse.auth.application.ports.TokenGenerator;
import com.rahul.pulse.auth.application.ports.TokenHasher;
import com.rahul.pulse.auth.domain.exception.InvalidCredentialsException;
import com.rahul.pulse.auth.domain.model.Email;
import com.rahul.pulse.auth.domain.model.PasswordHash;
import com.rahul.pulse.auth.domain.model.User;
import com.rahul.pulse.auth.domain.repository.RefreshTokenRepository;
import com.rahul.pulse.auth.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginUserUseCaseTest {

    private UserRepository userRepository;
    private TokenGenerator tokenGenerator;
    private PasswordHasher passwordHasher;
    private RefreshTokenRepository refreshTokenRepository;
    private TokenHasher tokenHasher;

    private LoginUserUseCase loginUserUseCase;

    @BeforeEach
    void setup()
    {
        userRepository = mock(UserRepository.class);
        tokenGenerator = mock(TokenGenerator.class);
        passwordHasher = mock(PasswordHasher.class);
        tokenHasher = mock(TokenHasher.class);
        refreshTokenRepository = mock(RefreshTokenRepository.class);

        loginUserUseCase = new LoginUserUseCaseImpl(
                userRepository,
                refreshTokenRepository,
                tokenGenerator,
                passwordHasher,
                tokenHasher
        );
    }

    @Test
    void should_login_user_successfully()
    {
        LoginUserCommand command = new LoginUserCommand(
                "example@domain.com",
                "example123"
        );

        Email email = new Email(command.email());

        User user = User.create(
                email,
                new PasswordHash("hashed-password"),
                "John",
                "Doe"
        );

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(passwordHasher.matches(command.password(), user.getPasswordHash().value()))
                .thenReturn(true);

        when(tokenGenerator.generateAccessToken(user))
                .thenReturn("access-token");

        when(tokenGenerator.generateRefreshToken(user))
                .thenReturn("refresh-token");

        LoginUserResult result = loginUserUseCase.login(command);

        assertNotNull(result);
        assertEquals("access-token", result.accessToken());
        assertEquals("refresh-token", result.refreshToken());
        assertEquals(user.getId().value().toString(), result.userId());

        verify(passwordHasher).matches(command.password(), user.getPasswordHash().value());
        verify(userRepository).findByEmail(email);
        verify(tokenGenerator).generateAccessToken(user);
        verify(tokenGenerator).generateRefreshToken(user);
    }

    @Test
    void should_throw_invalid_credentials_when_user_not_found()
    {
        LoginUserCommand command = new LoginUserCommand(
                "example@domain.com",
                "example123"
        );

        Email email = new Email(command.email());

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidCredentialsException.class,
                () -> loginUserUseCase.login(command)
        );

        verify(passwordHasher, never()).matches(anyString(), anyString());
        verify(tokenGenerator, never()).generateRefreshToken(any(User.class));
        verify(tokenGenerator, never()).generateAccessToken(any(User.class));
    }

    @Test
    void should_throw_invalid_credentials_when_password_mismatch()
    {
        LoginUserCommand command = new LoginUserCommand(
                "example@domain.com",
                "example123"
        );

        Email email = new Email(command.email());

        User user = User.create(
                email,
                new PasswordHash("hashed-password"),
                "John",
                "Doe"
        );

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(passwordHasher.matches(command.password(), user.getPasswordHash().value()))
                .thenReturn(false);

        assertThrows(
                InvalidCredentialsException.class,
                () -> loginUserUseCase.login(command)
        );

        verify(passwordHasher).matches(command.password(), "hashed-password");
        verify(tokenGenerator, never()).generateRefreshToken(any(User.class));
        verify(tokenGenerator, never()).generateAccessToken(any(User.class));
    }

}
