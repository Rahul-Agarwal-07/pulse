package com.rahul.pulse.auth.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.UUID;

@Getter
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    private UUID id;

    @Column(
            nullable = false,
            unique = true
    )
    private String email;

    @Column(
            name = "password_hash",
            nullable = false
    )
    private String passwordHash;

    private String fullName;

    protected UserEntity() {}

    public UserEntity(
            UUID id,
            String email,
            String passwordHash,
            String fullName
    ) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
    }

}