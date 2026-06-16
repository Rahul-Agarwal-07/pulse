package com.rahul.pulse.posts.infrastructure.persistence.repository;

import com.rahul.pulse.posts.infrastructure.persistence.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaPostRepository extends JpaRepository<PostEntity, UUID> {

    List<Optional<PostEntity>> findByAuthorId(UUID id);

}
