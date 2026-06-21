package com.rahul.pulse.posts.domain.model;

import com.rahul.pulse.auth.domain.model.UserId;

import java.time.Instant;
import java.util.UUID;

public record FeedPostView(
        UUID postId,
        UUID authorId,
        String authorName,
        String caption,
        String imageUrl,
        Integer likesCount,
        Integer commentCount,
        Instant createdAt
) {

    public FeedPostView(
            UUID postId,
            UUID authorId,
            String authorName,
            String caption,
            String imageUrl,
            Integer likesCount,
            Integer commentCount,
            Instant createdAt
    ) {
        this.postId = postId;
        this.authorId = authorId;
        this.authorName = authorName;
        this.caption = caption;
        this.imageUrl = imageUrl;
        this.likesCount = likesCount;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
    }
}
