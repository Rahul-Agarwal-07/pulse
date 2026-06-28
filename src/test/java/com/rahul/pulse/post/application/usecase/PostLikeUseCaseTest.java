package com.rahul.pulse.post.application.usecase;

import com.rahul.pulse.auth.domain.model.UserId;
import com.rahul.pulse.posts.application.dto.PostLikeCommand;
import com.rahul.pulse.posts.application.ports.PostLikeUseCase;
import com.rahul.pulse.posts.application.usecase.PostLikeUseCaseImpl;
import com.rahul.pulse.posts.domain.model.PostId;
import com.rahul.pulse.posts.domain.model.PostLike;
import com.rahul.pulse.posts.domain.repository.PostLikeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PostLikeUseCaseTest {

    private PostLikeRepository postLikeRepository;
    private PostLikeUseCase postLikeUseCase;

    @BeforeEach
    void setup()
    {
        postLikeRepository = mock(PostLikeRepository.class);
        postLikeUseCase = new PostLikeUseCaseImpl(postLikeRepository);
    }

    @Test
    void should_like_the_post_successfully()
    {
        when(postLikeRepository.exists(any(UserId.class), any(PostId.class)))
                .thenReturn(false);

        PostLikeCommand command = new PostLikeCommand(
                new UserId(UUID.randomUUID()),
                new PostId(UUID.randomUUID())
        );

        postLikeUseCase.execute(command);

        ArgumentCaptor<PostLike> captor = ArgumentCaptor.forClass(PostLike.class);
        verify(postLikeRepository).save(captor.capture());

        PostLike saved = captor.getValue();

        assertEquals(command.postId(), saved.getPostId());
        assertEquals(command.userId(), saved.getUserId());

    }

    @Test
    void should_ignore_if_post_already_liked()
    {
        when(postLikeRepository.exists(any(UserId.class), any(PostId.class)))
                .thenReturn(true);

        PostLikeCommand command = new PostLikeCommand(
                new UserId(UUID.randomUUID()),
                new PostId(UUID.randomUUID())
        );

        postLikeUseCase.execute(command);

        verify(postLikeRepository, never()).save(any(PostLike.class));
    }


}
