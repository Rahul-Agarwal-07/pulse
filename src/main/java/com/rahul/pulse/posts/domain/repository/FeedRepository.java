package com.rahul.pulse.posts.domain.repository;

import com.rahul.pulse.auth.domain.model.UserId;
import com.rahul.pulse.posts.domain.model.FeedPostView;

import java.util.List;

public interface FeedRepository {

    List<FeedPostView> getFeed(
            UserId currentUser,
            Integer page,
            Integer size
    );

}
