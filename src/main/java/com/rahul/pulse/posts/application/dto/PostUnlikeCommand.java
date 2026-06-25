package com.rahul.pulse.posts.application.dto;

import com.rahul.pulse.auth.domain.model.UserId;
import com.rahul.pulse.posts.domain.model.PostId;

public record PostUnlikeCommand(
        UserId userId,
        PostId postId
) {
}
