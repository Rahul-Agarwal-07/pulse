package com.rahul.pulse.auth.domain.model;
import java.time.Instant;
import java.util.Objects;

public class User {

    private final UserId id;

    private Email email;

    private PasswordHash passwordHash;

    private String firstName;

    private String lastName;

    private final Instant createdAt;

    public User(
            UserId id,
            Email email,
            PasswordHash passwordHash,
            String firstName,
            String lastName,
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

        validateName(firstName);
        validateName(lastName);

        this.firstName = firstName.trim();
        this.lastName = lastName.trim();

        this.createdAt =
                Objects.requireNonNull(
                        createdAt
                );

    }

    public static User create(

            Email email,

            PasswordHash passwordHash,

            String firstName,

            String lastName

    ) {

        return new User(

                UserId.generate(),

                email,

                passwordHash,

                firstName,

                lastName,

                Instant.now()

        );

    }

    public static User restore(
            Email email,
            PasswordHash passwordHash,
            String firstName,
            String lastName,
            UserId id
    )
    {
        return new User(
                id,
                email,
                passwordHash,
                firstName,
                lastName,
                Instant.now()
        );
    }

    private void validateName(
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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

}