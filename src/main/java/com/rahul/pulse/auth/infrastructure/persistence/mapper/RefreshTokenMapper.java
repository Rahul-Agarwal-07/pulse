package com.rahul.pulse.auth.infrastructure.persistence.mapper;

import com.rahul.pulse.auth.domain.model.RefreshToken;
import com.rahul.pulse.auth.domain.model.UserId;
import com.rahul.pulse.auth.infrastructure.persistence.entity.RefreshTokenEntity;

public class RefreshTokenMapper {

    static public RefreshTokenEntity toEntity(RefreshToken refreshToken)
    {
        return new RefreshTokenEntity(
                refreshToken.getId().id(),
                refreshToken.getUserId().value(),
                refreshToken.getTokenHash(),
                refreshToken.isRevoked(),
                refreshToken.getExpiresAt(),
                refreshToken.getCreatedAt()
        );
    }

    static public RefreshToken toDomain(RefreshTokenEntity entity)
    {
        return new RefreshToken(
                new UserId(entity.getUserId()),
                entity.getTokenHash(),
                entity.getExpiresAt(),
                entity.isRevoked(),
                entity.getCreatedAt()
        );
    }

}
