package com.rahul.pulse.auth.application.usecase;

import com.rahul.pulse.auth.application.dto.LoginUserCommand;
import com.rahul.pulse.auth.application.dto.LoginUserResult;
import com.rahul.pulse.auth.application.ports.LoginUserUseCase;
import com.rahul.pulse.auth.application.ports.PasswordHasher;
import com.rahul.pulse.auth.application.ports.TokenGenerator;
import com.rahul.pulse.auth.domain.exception.InvalidCredentialsException;
import com.rahul.pulse.auth.domain.model.Email;
import com.rahul.pulse.auth.domain.model.User;
import com.rahul.pulse.auth.domain.repository.UserRepository;

public class LoginUserUseCaseImpl implements LoginUserUseCase {

    final UserRepository userRepository;
    final TokenGenerator tokenGenerator;
    final PasswordHasher passwordHasher;

    public LoginUserUseCaseImpl(UserRepository userRepository, TokenGenerator tokenGenerator, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.tokenGenerator = tokenGenerator;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public LoginUserResult login(LoginUserCommand command) {

        Email email = new Email(command.email());

        User user = userRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        String passwordHash = user.getPasswordHash().value();

        if(!passwordHasher.matches(command.password(), passwordHash))
            throw new InvalidCredentialsException();

        String accessToken = tokenGenerator.generateAccessToken(user);
        String refreshToken = tokenGenerator.generateRefreshToken(user);

        String userId = user.getId().value().toString();

        return new LoginUserResult(
                userId,
                accessToken,
                refreshToken
        );
    }
}
