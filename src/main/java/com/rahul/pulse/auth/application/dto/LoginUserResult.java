package com.rahul.pulse.auth.application.dto;

public record LoginUserResult(
        String userId,
        String accessToken,
        String refreshToken
) {
}
