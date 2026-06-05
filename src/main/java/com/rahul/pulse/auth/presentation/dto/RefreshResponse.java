package com.rahul.pulse.auth.presentation.dto;

public record RefreshResponse(
        String accessToken,
        String refreshToken
) {
}
