package com.rahul.pulse.posts.application.usecase;

import com.rahul.pulse.posts.application.dto.PostLikeCommand;
import com.rahul.pulse.posts.application.ports.PostLikeUseCase;
import com.rahul.pulse.posts.domain.exception.InvalidPostException;
import com.rahul.pulse.posts.domain.model.Post;
import com.rahul.pulse.posts.domain.model.PostLike;
import com.rahul.pulse.posts.domain.repository.PostLikeRepository;
import com.rahul.pulse.posts.domain.repository.PostRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Transactional
public class PostLikeUseCaseImpl implements PostLikeUseCase {

    final PostLikeRepository postLikeRepository;
    final PostRepository postRepository;

    public PostLikeUseCaseImpl(PostLikeRepository postLikeRepository, PostRepository postRepository) {
        this.postLikeRepository = postLikeRepository;
        this.postRepository = postRepository;
    }

    @Override
    public void execute(PostLikeCommand command) {

        if(postLikeRepository.exists(command.userId(), command.postId()))
            return;

        Post post = postRepository.findById(command.postId())
                .orElseThrow(InvalidPostException::new);

        post.incrementLikesCount();

        postRepository.save(post);

        PostLike postLike = new PostLike(
                command.userId(),
                command.postId(),
                Instant.now()
        );

        postLikeRepository.save(postLike);
    }
}
