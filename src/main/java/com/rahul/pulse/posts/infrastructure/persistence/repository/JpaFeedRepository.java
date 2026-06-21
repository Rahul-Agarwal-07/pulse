package com.rahul.pulse.posts.infrastructure.persistence.repository;

import com.rahul.pulse.posts.domain.model.FeedPostView;
import com.rahul.pulse.posts.infrastructure.persistence.entity.PostEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface JpaFeedRepository extends JpaRepository<PostEntity, UUID> {

    @Query("""
            SELECT new com.rahul.pulse.posts.domain.model.FeedPostView(
                p.id,
                p.authorId,
                CONCAT(CONCAT(u.firstName, ' '), u.lastName),
                p.caption,
                p.imageUrl,
                p.likesCount,
                p.commentCount,
                p.createdAt
            )
            FROM PostEntity p
            JOIN UserEntity u
                ON p.authorId = u.id
            ORDER BY p.createdAt DESC
            """)
    List<FeedPostView> getFeed(Pageable pageable);

}
