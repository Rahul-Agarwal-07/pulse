package com.rahul.pulse.posts.domain.model;

import com.rahul.pulse.auth.domain.model.UserId;
import lombok.Getter;

import java.time.Instant;

@Getter
public class Post {

    private final PostId id;
    private final UserId authorId;

    private String caption;
    private String imageUrl;
    private Integer likesCount;
    private Integer commentCount;

    private Instant updatedAt;
    private final Instant createdAt;

    private Post(
            PostId id,
            UserId authorId,
            Instant createdAt,
            String caption,
            String imageUrl,
            Integer likesCount,
            Integer commentCount,
            Instant updatedAt
    ) {
        this.id = id;
        this.authorId = authorId;
        this.createdAt = createdAt;
        this.caption = caption;
        this.imageUrl = imageUrl;
        this.likesCount = likesCount;
        this.commentCount = commentCount;
        this.updatedAt = updatedAt;
    }

    public static Post create(
            UserId authorId,
            String caption,
            String imageUrl,
            Instant updatedAt
    ){
        return new Post(
                PostId.generate(),
                authorId,
                Instant.now(),
                caption,
                imageUrl,
                0,
                0,
                null
        );
    }

    public static Post restore(
            PostId id,
            UserId authorId,
            Instant createdAt,
            String caption,
            String imageUrl,
            Integer likesCount,
            Integer commentCount,
            Instant updatedAt
    )
    {
        return new Post(
                id,
                authorId,
                createdAt,
                caption,
                imageUrl,
                likesCount,
                commentCount,
                updatedAt
        );
    }

    public void updateCaption(String updatedCaption)
    {
        this.caption = updatedCaption;
    }

    public void incrementLikesCount()
    {
        this.likesCount++;
    }

    public void decrementLikesCount()
    {
        if(likesCount > 0) likesCount--;
    }

    public void incrementCommentCount()
    {
        commentCount++;
    }

    public void decrementCommentCount()
    {
        if(commentCount > 0) commentCount--;
    }


}
