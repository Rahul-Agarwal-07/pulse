package com.rahul.pulse.posts.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "post_likes")
public class PostLikeEntity {

    @Id
    private UUID id;

    private UUID userId;

    private UUID postId;

    private Instant createdAt;

    protected PostLikeEntity() {}

    public PostLikeEntity(UUID postLikeId, UUID userId, UUID postId, Instant createdAt) {
        this.id = postLikeId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.postId = postId;
    }
}
