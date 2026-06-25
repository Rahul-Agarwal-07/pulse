package com.rahul.pulse.posts.application.usecase;

import com.rahul.pulse.posts.application.dto.PostUnlikeCommand;
import com.rahul.pulse.posts.application.ports.PostUnlikeUseCase;
import com.rahul.pulse.posts.domain.exception.LikeDoesNotExistsException;
import com.rahul.pulse.posts.domain.repository.PostLikeRepository;

public class PostUnlikeUseCaseImpl implements PostUnlikeUseCase {

    final PostLikeRepository postLikeRepository;

    public PostUnlikeUseCaseImpl(PostLikeRepository postLikeRepository) {
        this.postLikeRepository = postLikeRepository;
    }

    @Override
    public void execute(PostUnlikeCommand command) {

        if(!postLikeRepository.exists(command.userId(), command.postId()))
            throw new LikeDoesNotExistsException();

        postLikeRepository.delete(command.userId(), command.postId());
    }
}
