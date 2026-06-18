package com.rahul.pulse.posts.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Table(name = "posts")
public class PostEntity {

    @Id
    private UUID id;

    @Column(nullable = false)
    private UUID authorId;

    @Column(nullable = false)
    private String caption;

    @Column(nullable = false)
    private String imageUrl;

    private Integer likesCount;
    private Integer commentCount;
    private Instant createdAt;
    private Instant updatedAt;

    protected PostEntity() {}

    public PostEntity(
            UUID id,
            UUID authorId,
            String caption,
            String imageUrl,
            Integer likesCount,
            Integer commentCount,
            Instant createdAt,
            Instant updatedAt
    ) {
        this.id = id;
        this.authorId = authorId;
        this.caption = caption;
        this.imageUrl = imageUrl;
        this.likesCount = likesCount;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
