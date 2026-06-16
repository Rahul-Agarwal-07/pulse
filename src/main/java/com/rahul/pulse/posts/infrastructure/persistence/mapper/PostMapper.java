package com.rahul.pulse.posts.infrastructure.persistence.mapper;

import com.rahul.pulse.auth.domain.model.UserId;
import com.rahul.pulse.posts.domain.model.Post;
import com.rahul.pulse.posts.domain.model.PostId;
import com.rahul.pulse.posts.infrastructure.persistence.entity.PostEntity;

public class PostMapper {

    public static Post toDomain(PostEntity postEntity)
    {
        return Post.restore(
                new PostId(postEntity.getId()),
                new UserId(postEntity.getAuthorId()),
                postEntity.getCreatedAt(),
                postEntity.getCaption(),
                postEntity.getImageUrl(),
                postEntity.getLikesCount(),
                postEntity.getCommentCount(),
                postEntity.getUpdatedAt()
        );
    }

    public static PostEntity toEntity(Post post)
    {
        return new PostEntity(
                post.getId().value(),
                post.getAuthorId().value(),
                post.getCaption(),
                post.getImageUrl(),
                post.getLikesCount(),
                post.getCommentCount(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }

}
