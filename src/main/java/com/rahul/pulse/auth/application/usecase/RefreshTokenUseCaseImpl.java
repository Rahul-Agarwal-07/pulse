package com.rahul.pulse.auth.application.usecase;

import com.rahul.pulse.auth.application.dto.RefreshTokenCommand;
import com.rahul.pulse.auth.application.dto.RefreshTokenResult;
import com.rahul.pulse.auth.application.ports.RefreshTokenUseCase;
import com.rahul.pulse.auth.application.ports.TokenGenerator;
import com.rahul.pulse.auth.application.ports.TokenParser;
import com.rahul.pulse.auth.domain.exception.InvalidCredentialsException;
import com.rahul.pulse.auth.domain.model.User;
import com.rahul.pulse.auth.domain.model.UserId;
import com.rahul.pulse.auth.domain.repository.UserRepository;

import java.util.UUID;

public class RefreshTokenUseCaseImpl implements RefreshTokenUseCase {

    final TokenGenerator tokenGenerator;
    final TokenParser tokenParser;
    final UserRepository userRepository;

    public RefreshTokenUseCaseImpl(TokenGenerator tokenGenerator, TokenParser tokenParser, UserRepository userRepository) {
        this.tokenGenerator = tokenGenerator;
        this.tokenParser = tokenParser;
        this.userRepository = userRepository;
    }


    @Override
    public RefreshTokenResult refresh(RefreshTokenCommand command) {

        if (!tokenParser.isValid(command.refreshToken()))
            throw new InvalidCredentialsException("Invalid Token");

        if (!tokenParser.isRefreshToken(command.refreshToken()))
            throw new InvalidCredentialsException("Invalid Token");

        String userId = tokenParser.extractUserId(command.refreshToken());

        User user = userRepository.findById(
                new UserId(UUID.fromString(userId))
        ).orElseThrow(InvalidCredentialsException::new);

        String refreshToken = tokenGenerator.generateRefreshToken(user);
        String accessToken = tokenGenerator.generateAccessToken(user);

        return new RefreshTokenResult(
                refreshToken,
                accessToken
        );
    }
}
