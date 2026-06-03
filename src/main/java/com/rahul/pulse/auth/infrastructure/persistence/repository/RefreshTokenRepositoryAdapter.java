package com.rahul.pulse.auth.infrastructure.persistence.repository;

import com.rahul.pulse.auth.domain.model.RefreshToken;
import com.rahul.pulse.auth.domain.model.RefreshTokenId;
import com.rahul.pulse.auth.domain.model.UserId;
import com.rahul.pulse.auth.domain.repository.RefreshTokenRepository;
import com.rahul.pulse.auth.infrastructure.persistence.mapper.RefreshTokenMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class RefreshTokenRepositoryAdapter implements RefreshTokenRepository {

    private final JpaRefreshTokenRepository jpaRefreshTokenRepository;

    public RefreshTokenRepositoryAdapter(JpaRefreshTokenRepository refreshTokenRepository) {
        this.jpaRefreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void save(RefreshToken token) {

        jpaRefreshTokenRepository.save(RefreshTokenMapper.toEntity(token));

    }

    @Override
    public Optional<RefreshToken> findByTokenHash(String hash) {

        return jpaRefreshTokenRepository
                .findByTokenHash(hash)
                .map(RefreshTokenMapper::toDomain);
    }

    @Override
    public int revoke(RefreshTokenId tokenId) {
        return jpaRefreshTokenRepository.revoke(tokenId.id());
    }

    @Override
    public int revokeAllByUserId(UserId userId) {
        return jpaRefreshTokenRepository.revokeAllByUserId(userId.value());
    }
}
