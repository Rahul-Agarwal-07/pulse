package com.rahul.pulse.posts.domain.model;

import com.rahul.pulse.auth.domain.model.UserId;
import com.rahul.pulse.posts.domain.exception.InvalidPostException;
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
            String imageUrl
    ){

        if(caption == null || caption.isBlank())
            throw new InvalidPostException("Caption is required");

        if(imageUrl == null || imageUrl.isBlank())
            throw new InvalidPostException("Image is required");


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
        updatedAt = Instant.now();
    }

    public void incrementLikesCount()
    {
        this.likesCount++;
        updatedAt = Instant.now();
    }

    public void decrementLikesCount()
    {
        if(likesCount > 0) {
            likesCount--;
            updatedAt = Instant.now();
        }
    }

    public void incrementCommentCount()
    {
        commentCount++;
        updatedAt = Instant.now();
    }

    public void decrementCommentCount()
    {
        if(commentCount > 0) {
            commentCount--;
            updatedAt = Instant.now();
        }
    }

    public void updateImageUrl(String newUrl)
    {
        this.imageUrl = newUrl;
        this.updatedAt = Instant.now();
    }

}
