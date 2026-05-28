package com.rahul.pulse.auth.application.dto;

public record LoginUserCommand(
        String email,
        String password
) {
}
