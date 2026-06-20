package com.rahul.pulse.posts.domain.model;

import com.rahul.pulse.auth.domain.model.UserId;

import java.time.Instant;

public record FeedPostView(
        PostId postId,
        UserId authorId,
        String authorName,
        String caption,
        String imageUrl,
        Integer likesCount,
        Integer commentCount,
        Instant createdAt
) {
}
