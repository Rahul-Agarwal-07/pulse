package com.rahul.pulse.auth.domain.model;
import java.time.Instant;
import java.util.Objects;

public class User {

    private final UserId id;

    private Email email;

    private PasswordHash passwordHash;

    private String fullName;

    private final Instant createdAt;

    public User(
            UserId id,
            Email email,
            PasswordHash passwordHash,
            String fullName,
            Instant createdAt
    ) {

        this.id =
                Objects.requireNonNull(id);

        this.email =
                Objects.requireNonNull(email);

        this.passwordHash =
                Objects.requireNonNull(
                        passwordHash
                );

        validateFullName(
                fullName
        );

        this.fullName =
                fullName.trim();

        this.createdAt =
                Objects.requireNonNull(
                        createdAt
                );

    }

    public static User create(

            Email email,

            PasswordHash passwordHash,

            String fullName

    ) {

        return new User(

                UserId.generate(),

                email,

                passwordHash,

                fullName,

                Instant.now()

        );

    }

    public static User restore(
            Email email,
            PasswordHash passwordHash,
            String fullName,
            UserId id
    )
    {
        return new User(
                id,
                email,
                passwordHash,
                fullName,
                Instant.now()
        );
    }

    private void validateFullName(
            String fullName
    ) {

        Objects.requireNonNull(
                fullName
        );

        if(fullName.isBlank()) {

            throw new IllegalArgumentException(
                    "Full name cannot be blank"
            );

        }

    }

    public UserId getId() {
        return id;
    }

    public Email getEmail() {
        return email;
    }

    public PasswordHash getPasswordHash() {
        return passwordHash;
    }

    public String getFullName() {
        return fullName;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

}