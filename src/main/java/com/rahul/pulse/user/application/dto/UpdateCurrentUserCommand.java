package com.rahul.pulse.user.application.dto;

public record UpdateCurrentUserCommand(
        String userId,
        String newFirstName,
        String newLastName
) {

}
