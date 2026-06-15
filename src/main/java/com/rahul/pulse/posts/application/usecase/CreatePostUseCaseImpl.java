package com.rahul.pulse.posts.application.usecase;

import com.rahul.pulse.posts.application.dto.CreatePostCommand;
import com.rahul.pulse.posts.application.dto.CreatePostResult;
import com.rahul.pulse.posts.application.ports.CreatePostUseCase;
import com.rahul.pulse.posts.domain.model.Post;
import com.rahul.pulse.posts.domain.repository.PostRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CreatePostUseCaseImpl implements CreatePostUseCase {

    final PostRepository postRepository;

    public CreatePostUseCaseImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public CreatePostResult execute(CreatePostCommand command) {

        Post post = Post.create(
                command.authorId(),
                command.caption(),
                command.imageUrl()
        );

        postRepository.save(post);

        return new CreatePostResult(
                post.getId()
        );

    }
}
