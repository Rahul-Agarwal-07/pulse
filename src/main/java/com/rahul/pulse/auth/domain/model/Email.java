package com.rahul.pulse.auth.domain.model;

import com.rahul.pulse.auth.domain.exception.InvalidEmailException;

import java.util.Objects;
import java.util.regex.Pattern;

public record Email(String value) {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(
                    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
            );

    public Email {

        Objects.requireNonNull(value, "Email cannot be null");

        value = value.trim().toLowerCase();

        if (value.isBlank()) {
            throw new InvalidEmailException(
                    "Email cannot be blank"
            );
        }

        if (!EMAIL_PATTERN.matcher(value)
                .matches()) {
            throw new InvalidEmailException(
                    "Invalid email format"
            );
        }
    }

}