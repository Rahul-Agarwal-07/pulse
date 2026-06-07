package com.rahul.pulse.auth.application.usecase;

import com.rahul.pulse.auth.application.dto.LogoutCommand;
import com.rahul.pulse.auth.application.ports.LogoutUseCase;
import com.rahul.pulse.auth.application.ports.TokenHasher;
import com.rahul.pulse.auth.application.ports.TokenParser;
import com.rahul.pulse.auth.domain.exception.InvalidTokenException;
import com.rahul.pulse.auth.domain.model.RefreshToken;
import com.rahul.pulse.auth.domain.model.UserId;
import com.rahul.pulse.auth.domain.repository.RefreshTokenRepository;
import com.rahul.pulse.auth.domain.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
public class LogoutUseCaseImpl implements LogoutUseCase {

    final RefreshTokenRepository refreshTokenRepository;
    final UserRepository userRepository;
    final TokenParser parser;
    final TokenHasher hasher;

    public LogoutUseCaseImpl(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository, TokenParser parser, TokenHasher hasher) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.parser = parser;
        this.hasher = hasher;
    }

    @Override
    public void logout(LogoutCommand command) {

        String token = command.refreshToken();

        if(!parser.isValid(token))
            throw new InvalidTokenException();

        if(!parser.isRefreshToken(token))
            throw new InvalidTokenException();

        UserId userId = new UserId(UUID.fromString(parser.extractUserId(token)));

        userRepository.findById(userId)
                .orElseThrow(InvalidTokenException::new);

        String tokenHash = hasher.hash(token);

        RefreshToken refreshToken = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(InvalidTokenException::new);

        if(refreshToken.isRevoked()) return;

        int updated = refreshTokenRepository.revoke(refreshToken.getId());

        if(updated == 0)
            throw new InvalidTokenException();

        return;
    }
}
