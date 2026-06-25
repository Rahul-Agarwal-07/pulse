package com.rahul.pulse.posts.domain.repository;

import com.rahul.pulse.auth.domain.model.UserId;
import com.rahul.pulse.posts.domain.model.PostId;
import com.rahul.pulse.posts.domain.model.PostLike;

public interface PostLikeRepository {

    void save(PostLike postLike);
    boolean exists(UserId userId, PostId postId);
    void delete(UserId userId, PostId postId);

}
