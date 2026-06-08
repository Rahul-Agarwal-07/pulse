package com.rahul.pulse.user.presentation.dto;

public record GetCurrentUserResponse(
        String userId,
        String email,
        String firstName,
        String lastName
) {
}
