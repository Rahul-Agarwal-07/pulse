package com.rahul.pulse.posts.application.usecase;

import com.rahul.pulse.posts.application.dto.GetFeedCommand;
import com.rahul.pulse.posts.application.dto.GetFeedResult;
import com.rahul.pulse.posts.application.ports.GetFeedUseCase;
import com.rahul.pulse.posts.domain.model.FeedPostView;
import com.rahul.pulse.posts.domain.repository.FeedRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class GetFeedUseCaseImpl implements GetFeedUseCase {

    final FeedRepository feedRepository;

    public GetFeedUseCaseImpl(FeedRepository feedRepository) {
        this.feedRepository = feedRepository;
    }

    @Override
    public GetFeedResult execute(GetFeedCommand command) {

        List<FeedPostView> feed = feedRepository.getFeed(
                command.currentUser(),
                command.page(),
                command.size()
        );

        return new GetFeedResult(
                feed
        );

    }
}
