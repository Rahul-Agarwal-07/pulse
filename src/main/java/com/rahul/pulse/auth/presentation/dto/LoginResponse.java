package com.rahul.pulse.auth.presentation.dto;

public record LoginResponse(
        String userId,
        String accessToken,
        String refreshToken
) {
}
