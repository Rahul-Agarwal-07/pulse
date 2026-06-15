package com.rahul.pulse.posts.application.dto;

import com.rahul.pulse.auth.domain.model.UserId;

public record CreatePostCommand(
        UserId authorId,
        String caption,
        String imageUrl
) {
}
