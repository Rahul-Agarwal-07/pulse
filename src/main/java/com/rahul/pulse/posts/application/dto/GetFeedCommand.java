package com.rahul.pulse.posts.application.dto;

import com.rahul.pulse.auth.domain.model.UserId;

public record GetFeedCommand(
        UserId currentUser,
        Integer page,
        Integer size
) {
}
