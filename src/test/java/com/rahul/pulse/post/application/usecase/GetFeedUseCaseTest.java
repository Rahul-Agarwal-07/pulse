package com.rahul.pulse.post.application.usecase;

import com.rahul.pulse.auth.domain.model.UserId;
import com.rahul.pulse.posts.application.dto.GetFeedCommand;
import com.rahul.pulse.posts.application.dto.GetFeedResult;
import com.rahul.pulse.posts.application.ports.GetFeedUseCase;
import com.rahul.pulse.posts.application.usecase.GetFeedUseCaseImpl;
import com.rahul.pulse.posts.domain.model.FeedPostView;
import com.rahul.pulse.posts.domain.repository.FeedRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GetFeedUseCaseTest {

    private FeedRepository feedRepository;
    private GetFeedUseCase getFeedUseCase;

    @BeforeEach
    void setup()
    {
        feedRepository = mock(FeedRepository.class);
        getFeedUseCase = new GetFeedUseCaseImpl(feedRepository);
    }

    @Test
    void should_return_feed_successfully()
    {
        GetFeedCommand command = new GetFeedCommand(
                new UserId(UUID.randomUUID()),
                0,
                10
        );


        List<FeedPostView> feed = new ArrayList<>();

        feed.add(
                new FeedPostView(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        "author1",
                        "caption1",
                        "image1",
                        0,
                        0,
                        Instant.now()
                )
        );

        feed.add(
                new FeedPostView(
                        UUID.randomUUID(),
                        UUID.randomUUID(),
                        "author2",
                        "caption2",
                        "image2",
                        0,
                        0,
                        Instant.now()
                )
        );

        when(feedRepository.getFeed(
                command.currentUser(),
                command.page(),
                command.size()
        ))
                .thenReturn(feed);

        GetFeedResult result = getFeedUseCase.execute(command);

        assertEquals(result.content().size(), feed.size());
    }

}
