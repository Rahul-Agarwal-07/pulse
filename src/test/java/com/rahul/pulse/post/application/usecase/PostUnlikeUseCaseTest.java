package com.rahul.pulse.post.application.usecase;

import com.rahul.pulse.auth.domain.model.UserId;
import com.rahul.pulse.posts.application.dto.PostLikeCommand;
import com.rahul.pulse.posts.application.dto.PostUnlikeCommand;
import com.rahul.pulse.posts.application.ports.PostLikeUseCase;
import com.rahul.pulse.posts.application.ports.PostUnlikeUseCase;
import com.rahul.pulse.posts.application.usecase.PostLikeUseCaseImpl;
import com.rahul.pulse.posts.application.usecase.PostUnlikeUseCaseImpl;
import com.rahul.pulse.posts.domain.model.PostId;
import com.rahul.pulse.posts.domain.model.PostLike;
import com.rahul.pulse.posts.domain.repository.PostLikeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PostUnlikeUseCaseTest {

    private PostLikeRepository postLikeRepository;
    private PostUnlikeUseCase postUnlikeUseCase;
    private PostLikeUseCase postLikeUseCase;

    @BeforeEach
    void setup()
    {
        postLikeRepository = mock(PostLikeRepository.class);
        postUnlikeUseCase = new PostUnlikeUseCaseImpl(postLikeRepository);
        postLikeUseCase = new PostLikeUseCaseImpl(postLikeRepository);
    }

    @Test
    void should_unlike_the_post_successfully()
    {
        PostUnlikeCommand command = new PostUnlikeCommand(
                new UserId(UUID.randomUUID()),
                new PostId(UUID.randomUUID())
        );

        when(postLikeRepository.exists(command.userId(), command.postId()))
                .thenReturn(true);

        postUnlikeUseCase.execute(command);

        verify(postLikeRepository).delete(any(), any());
    }

}
