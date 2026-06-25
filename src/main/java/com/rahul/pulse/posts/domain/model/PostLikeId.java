package com.rahul.pulse.posts.domain.model;

import java.util.Objects;
import java.util.UUID;

public record PostLikeId(
        UUID value
) {
    public PostLikeId {

        Objects.requireNonNull(
                value,
                "User id cannot be null"
        );

    }

    public static PostLikeId generate() {

        return new PostLikeId(
                UUID.randomUUID()
        );

    }
}
