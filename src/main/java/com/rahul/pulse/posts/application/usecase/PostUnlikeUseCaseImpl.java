package com.rahul.pulse.posts.application.usecase;

import com.rahul.pulse.posts.application.dto.PostUnlikeCommand;
import com.rahul.pulse.posts.application.ports.PostUnlikeUseCase;
import com.rahul.pulse.posts.domain.exception.InvalidPostException;
import com.rahul.pulse.posts.domain.exception.LikeDoesNotExistsException;
import com.rahul.pulse.posts.domain.model.Post;
import com.rahul.pulse.posts.domain.repository.PostLikeRepository;
import com.rahul.pulse.posts.domain.repository.PostRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class PostUnlikeUseCaseImpl implements PostUnlikeUseCase {

    final PostLikeRepository postLikeRepository;
    final PostRepository postRepository;

    public PostUnlikeUseCaseImpl(PostLikeRepository postLikeRepository, PostRepository postRepository) {
        this.postLikeRepository = postLikeRepository;
        this.postRepository = postRepository;
    }

    @Override
    public void execute(PostUnlikeCommand command) {

        if(!postLikeRepository.exists(command.userId(), command.postId()))
            throw new LikeDoesNotExistsException();

        Post post = postRepository.findById(command.postId())
                        .orElseThrow(InvalidPostException::new);

        post.decrementLikesCount();

        postRepository.save(post);

        postLikeRepository.delete(command.userId(), command.postId());
    }
}
