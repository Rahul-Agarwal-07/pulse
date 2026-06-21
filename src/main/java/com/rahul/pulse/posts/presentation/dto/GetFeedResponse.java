package com.rahul.pulse.posts.presentation.dto;

import java.util.List;

public record GetFeedResponse(
        List<FeedPostResponse> feed
) {
}
