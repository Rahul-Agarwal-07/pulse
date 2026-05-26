package com.rahul.pulse.auth.application.dto;

public record RegisterUserCommand(
        String email,
        String password,
        String firstName,
        String lastName
) {
}
