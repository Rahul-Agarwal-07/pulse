package com.rahul.pulse.posts.infrastructure.persistence.mapper;

import com.rahul.pulse.posts.domain.model.PostLike;
import com.rahul.pulse.posts.infrastructure.persistence.entity.PostLikeEntity;

public class PostLikeMapper {

    public static PostLikeEntity toEntity(PostLike postLike)
    {
        return new PostLikeEntity(
                postLike.getId().value(),
                postLike.getUserId().value(),
                postLike.getPostId().value(),
                postLike.getLikedAt()
        );

    }

}
