package com.rahul.pulse.auth.application.dto;

public record LogoutCommand(
        String refreshToken
) { }
