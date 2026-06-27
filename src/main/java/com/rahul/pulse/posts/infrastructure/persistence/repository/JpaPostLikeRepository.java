package com.rahul.pulse.posts.infrastructure.persistence.repository;

import com.rahul.pulse.posts.infrastructure.persistence.entity.PostLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaPostLikeRepository extends JpaRepository<PostLikeEntity, UUID> {

    boolean existsByPostIdAndUserId(UUID postId, UUID userId);
    void deleteByPostIdAndUserId(UUID postId, UUID userId);

}
