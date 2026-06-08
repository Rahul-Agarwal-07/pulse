package com.rahul.pulse.user.application.dto;

public record GetCurrentUserResult(
        String userId,
        String email,
        String firstName,
        String lastName
) {
}
