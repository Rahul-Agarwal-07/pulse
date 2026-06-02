package com.rahul.pulse.auth.application.usecase;

import com.rahul.pulse.auth.application.dto.LoginUserCommand;
import com.rahul.pulse.auth.application.dto.LoginUserResult;
import com.rahul.pulse.auth.application.ports.LoginUserUseCase;
import com.rahul.pulse.auth.application.ports.PasswordHasher;
import com.rahul.pulse.auth.application.ports.TokenGenerator;
import com.rahul.pulse.auth.application.ports.TokenHasher;
import com.rahul.pulse.auth.domain.exception.InvalidCredentialsException;
import com.rahul.pulse.auth.domain.model.Email;
import com.rahul.pulse.auth.domain.model.RefreshToken;
import com.rahul.pulse.auth.domain.model.User;
import com.rahul.pulse.auth.domain.repository.RefreshTokenRepository;
import com.rahul.pulse.auth.domain.repository.UserRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

public class LoginUserUseCaseImpl implements LoginUserUseCase {

    final UserRepository userRepository;
    final RefreshTokenRepository refreshTokenRepository;
    final TokenGenerator tokenGenerator;
    final PasswordHasher passwordHasher;
    final TokenHasher tokenHasher;

    public LoginUserUseCaseImpl(
            UserRepository userRepository,
            RefreshTokenRepository refreshTokenRepository,
            TokenGenerator tokenGenerator,
            PasswordHasher passwordHasher,
            TokenHasher tokenHasher
    ) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenGenerator = tokenGenerator;
        this.passwordHasher = passwordHasher;
        this.tokenHasher = tokenHasher;
    }

    @Override
    public LoginUserResult login(LoginUserCommand command) {

        Email email = new Email(command.email());

        User user = userRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        String passwordHash = user.getPasswordHash().value();

        if(!passwordHasher.matches(command.password(), passwordHash))
            throw new InvalidCredentialsException();

        String refreshToken =  tokenGenerator.generateRefreshToken(user);
        String refreshTokenHash = tokenHasher.hash(refreshToken);

        Instant now = Instant.now();

        RefreshToken token = new RefreshToken(
                user.getId(),
                refreshTokenHash,
                now.plus(7, ChronoUnit.DAYS),
                false,
                now
        );

        refreshTokenRepository.save(token);

        String accessToken = tokenGenerator.generateAccessToken(user);

        String userId = user.getId().value().toString();

        return new LoginUserResult(
                userId,
                accessToken,
                refreshToken
        );
    }
}
