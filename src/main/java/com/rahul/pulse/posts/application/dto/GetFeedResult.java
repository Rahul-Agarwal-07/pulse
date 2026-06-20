package com.rahul.pulse.posts.application.dto;
import com.rahul.pulse.posts.domain.model.FeedPostView;

import java.util.List;

public record GetFeedResult(
        List<FeedPostView> content
) {
}
