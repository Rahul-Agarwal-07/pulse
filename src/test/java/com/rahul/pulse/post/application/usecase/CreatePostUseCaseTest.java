package com.rahul.pulse.post.application.usecase;

import com.rahul.pulse.auth.domain.model.UserId;
import com.rahul.pulse.posts.application.dto.CreatePostCommand;
import com.rahul.pulse.posts.application.dto.CreatePostResult;
import com.rahul.pulse.posts.application.ports.CreatePostUseCase;
import com.rahul.pulse.posts.application.usecase.CreatePostUseCaseImpl;
import com.rahul.pulse.posts.domain.model.Post;
import com.rahul.pulse.posts.domain.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CreatePostUseCaseTest {

    private PostRepository postRepository;
    private CreatePostUseCase createPostUseCase;

    @BeforeEach
    void setup()
    {
        postRepository = mock(PostRepository.class);
        createPostUseCase = new CreatePostUseCaseImpl(postRepository);
    }

    @Test
    void should_create_post_successfully()
    {
        CreatePostCommand command = new CreatePostCommand(
                new UserId(UUID.randomUUID()),
                "caption",
                "image-url"
        );

        CreatePostResult result = createPostUseCase.execute(command);

        ArgumentCaptor<Post> captor = ArgumentCaptor.forClass(Post.class);
        verify(postRepository).save(captor.capture());
        Post savedPost = captor.getValue();

        assertNotNull(savedPost);
        assertEquals(result.postId(), savedPost.getId());
        assertEquals(command.authorId(), savedPost.getAuthorId());
        assertEquals(command.caption(), savedPost.getCaption());
        assertEquals(command.imageUrl(), savedPost.getImageUrl());
    }
}
