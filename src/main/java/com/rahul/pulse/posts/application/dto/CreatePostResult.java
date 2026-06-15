package com.rahul.pulse.posts.application.dto;

import com.rahul.pulse.posts.domain.model.PostId;

public record CreatePostResult(
        PostId postId
) {
}
