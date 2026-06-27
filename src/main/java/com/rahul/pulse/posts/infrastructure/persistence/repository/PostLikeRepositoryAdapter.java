package com.rahul.pulse.posts.infrastructure.persistence.repository;

import com.rahul.pulse.auth.domain.model.UserId;
import com.rahul.pulse.posts.domain.model.PostId;
import com.rahul.pulse.posts.domain.model.PostLike;
import com.rahul.pulse.posts.domain.repository.PostLikeRepository;
import com.rahul.pulse.posts.infrastructure.persistence.mapper.PostLikeMapper;
import org.springframework.stereotype.Repository;

@Repository
public class PostLikeRepositoryAdapter implements PostLikeRepository {

    final JpaPostLikeRepository jpaPostLikeRepository;

    public PostLikeRepositoryAdapter(JpaPostLikeRepository jpaPostLikeRepository) {
        this.jpaPostLikeRepository = jpaPostLikeRepository;
    }

    @Override
    public void save(PostLike postLike) {
        jpaPostLikeRepository.save(PostLikeMapper.toEntity(postLike));
    }

    @Override
    public boolean exists(UserId userId, PostId postId) {
        return jpaPostLikeRepository.existsByPostIdAndUserId(postId.value(), userId.value());
    }

    @Override
    public void delete(UserId userId, PostId postId) {
        jpaPostLikeRepository.deleteByPostIdAndUserId(postId.value(), userId.value());
    }
}
