package com.rahul.pulse.posts.domain.model;

import com.rahul.pulse.auth.domain.model.UserId;
import lombok.Getter;

import java.time.Instant;

@Getter
public class PostLike {

    private final PostLikeId id;
    private final UserId userId;
    private final PostId postId;
    private final Instant likedAt;


    public PostLike(UserId userId, PostId postId, Instant likedAt) {
        this.id = PostLikeId.generate();
        this.userId = userId;
        this.postId = postId;
        this.likedAt = likedAt;
    }
}
