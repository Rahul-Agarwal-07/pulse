package com.rahul.pulse.auth.domain.repository;

import com.rahul.pulse.auth.domain.model.RefreshToken;
import com.rahul.pulse.auth.domain.model.RefreshTokenId;
import com.rahul.pulse.auth.domain.model.UserId;

import java.util.Optional;

public interface RefreshTokenRepository {

    void save(RefreshToken token);

    Optional<RefreshToken> findByTokenHash(String hash);

    void revoke(RefreshTokenId id);

    void revokeAllByUserId(UserId userId);

}
