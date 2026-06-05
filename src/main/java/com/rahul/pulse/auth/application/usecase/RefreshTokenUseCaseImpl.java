package com.rahul.pulse.auth.application.usecase;

import com.rahul.pulse.auth.application.dto.RefreshTokenCommand;
import com.rahul.pulse.auth.application.dto.RefreshTokenResult;
import com.rahul.pulse.auth.application.ports.*;
import com.rahul.pulse.auth.domain.exception.InvalidTokenException;
import com.rahul.pulse.auth.domain.exception.RefreshTokenReuseDetectedException;
import com.rahul.pulse.auth.domain.model.RefreshToken;
import com.rahul.pulse.auth.domain.model.User;
import com.rahul.pulse.auth.domain.repository.RefreshTokenRepository;
import com.rahul.pulse.auth.domain.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Transactional
public class RefreshTokenUseCaseImpl implements RefreshTokenUseCase {

    final TokenGenerator tokenGenerator;
    final TokenParser parser;
    final TokenHasher hasher;
    final RefreshTokenRepository refreshTokenRepository;
    final UserRepository userRepository;

    public RefreshTokenUseCaseImpl(TokenGenerator tokenGenerator, TokenParser parser, TokenHasher hasher, RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.tokenGenerator = tokenGenerator;
        this.parser = parser;
        this.hasher = hasher;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public RefreshTokenResult refresh(RefreshTokenCommand command) {

        String token = command.refreshToken();

        if(!parser.isValid(token))
            throw new InvalidTokenException();

        System.out.println("Token is Valid");

        if(!parser.isRefreshToken(token))
            throw new InvalidTokenException();

        System.out.println("Token is Refresh Token");

        String tokenHash = hasher.hash(token);

        RefreshToken refreshToken = refreshTokenRepository.findByTokenHash(tokenHash)
                .orElseThrow(InvalidTokenException::new);

        System.out.println("Token : " + refreshToken);

        if(refreshToken.isRevoked())
        {
            refreshTokenRepository.revokeAllByUserId(refreshToken.getUserId());
            throw new RefreshTokenReuseDetectedException();
        }

        Instant now = Instant.now();

        if(now.isAfter(refreshToken.getExpiresAt()))
            throw new InvalidTokenException("Token Expired");

        System.out.println("Token is not expired");

        User user = userRepository.findById(refreshToken.getUserId())
                .orElseThrow(InvalidTokenException::new);

        System.out.println("User : " + user);

        String newRefreshToken = tokenGenerator.generateRefreshToken(user);
        String newRefreshTokenHash = hasher.hash(newRefreshToken);

        int updated = refreshTokenRepository.revoke(refreshToken.getId());

        System.out.println("Updated rows = " + updated);

        if(updated == 0) throw new InvalidTokenException();

        System.out.println("Old token is revoked");

        RefreshToken newRefreshTokenModel = new RefreshToken(
                refreshToken.getUserId(),
                newRefreshTokenHash,
                now.plus(7, ChronoUnit.DAYS),
                false,
                now
        );

        refreshTokenRepository.save(newRefreshTokenModel);

        String accessToken = tokenGenerator.generateAccessToken(user);

        return new RefreshTokenResult(
                accessToken,
                newRefreshToken
        );
    }
}
