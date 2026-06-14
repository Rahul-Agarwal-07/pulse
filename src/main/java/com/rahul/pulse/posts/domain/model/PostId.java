package com.rahul.pulse.posts.domain.model;

import com.rahul.pulse.auth.domain.model.UserId;

import java.util.Objects;
import java.util.UUID;

public record PostId(
        UUID value
) {

    public PostId {

        Objects.requireNonNull(
                value,
                "User id cannot be null"
        );

    }

    public static PostId generate() {

        return new PostId(
                UUID.randomUUID()
        );

    }

}
