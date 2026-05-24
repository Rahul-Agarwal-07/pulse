package com.rahul.pulse.auth.infrastructure.persistence.mapper;

import com.rahul.pulse.auth.domain.model.Email;
import com.rahul.pulse.auth.domain.model.PasswordHash;
import com.rahul.pulse.auth.domain.model.User;
import com.rahul.pulse.auth.domain.model.UserId;
import com.rahul.pulse.auth.infrastructure.persistence.entity.UserEntity;

public class UserMapper {

    public static UserEntity toEntity(
            User user
    ) {
        return new UserEntity(
                user.getId()
                        .value(),

                user.getEmail()
                        .value(),

                user.getPasswordHash()
                        .value(),

                user.getFullName()
        );
    }

    public static User toDomain(
            UserEntity entity
    ) {

        return User.restore(
                new Email(
                        entity.getEmail()
                ),

                new PasswordHash(
                        entity
                                .getPasswordHash()
                ),

                entity.getFullName(),

                new UserId(
                        entity.getId()
                )
        );
    }

}
