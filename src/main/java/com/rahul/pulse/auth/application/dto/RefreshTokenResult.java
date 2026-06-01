package com.rahul.pulse.auth.application.dto;

public record RefreshTokenResult(
        String accessToken,
        String refreshToken
) {

}
