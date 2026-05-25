package com.rahul.pulse.auth.domain.model;

import com.rahul.pulse.auth.domain.exception.InvalidPasswordHashException;

import java.util.Objects;

public record PasswordHash(
        String value
) {

    public PasswordHash {

        Objects.requireNonNull(
                value,
                "Password hash cannot be null"
        );

        value = value.trim();

        if (value.isBlank()) {
            throw new InvalidPasswordHashException(
                    "Password hash cannot be blank"
            );
        }
    }

}