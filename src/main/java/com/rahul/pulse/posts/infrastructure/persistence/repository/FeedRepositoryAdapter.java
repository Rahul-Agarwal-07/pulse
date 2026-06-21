package com.rahul.pulse.posts.infrastructure.persistence.repository;

import com.rahul.pulse.auth.domain.model.UserId;
import com.rahul.pulse.posts.domain.model.FeedPostView;
import com.rahul.pulse.posts.domain.repository.FeedRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FeedRepositoryAdapter implements FeedRepository {

    private final JpaFeedRepository jpaFeedRepository;

    public FeedRepositoryAdapter(JpaFeedRepository jpaFeedRepository) {
        this.jpaFeedRepository = jpaFeedRepository;
    }

    @Override
    public List<FeedPostView> getFeed(UserId currentUser, Integer page, Integer size) {

        return jpaFeedRepository.getFeed(PageRequest.of(page, size));

    }
}
