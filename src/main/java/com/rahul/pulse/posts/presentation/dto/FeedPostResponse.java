package com.rahul.pulse.posts.presentation.dto;

import com.rahul.pulse.posts.domain.model.FeedPostView;

import java.time.Instant;

public record FeedPostResponse(
        String postId,
        String authorId,
        String authorName,
        String caption,
        String imageUrl,
        Integer likesCount,
        Integer commentCount,
        Instant createdAt
) {


    public static FeedPostResponse from(FeedPostView post)
    {
        return new FeedPostResponse(
                post.postId().toString(),
                post.authorId().toString(),
                post.authorName(),
                post.caption(),
                post.imageUrl(),
                post.likesCount(),
                post.commentCount(),
                post.createdAt()
        );
    }

}
