package com.rahul.pulse.posts.application.usecase;

import com.rahul.pulse.posts.application.dto.PostLikeCommand;
import com.rahul.pulse.posts.application.ports.PostLikeUseCase;
import com.rahul.pulse.posts.domain.model.PostLike;
import com.rahul.pulse.posts.domain.repository.PostLikeRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Transactional
public class PostLikeUseCaseImpl implements PostLikeUseCase {

    final PostLikeRepository postLikeRepository;

    public PostLikeUseCaseImpl(PostLikeRepository postLikeRepository) {
        this.postLikeRepository = postLikeRepository;
    }

    @Override
    public void execute(PostLikeCommand command) {


        if(postLikeRepository.exists(command.userId(), command.postId()))
            return;


        PostLike postLike = new PostLike(
                command.userId(),
                command.postId(),
                Instant.now()
        );

        postLikeRepository.save(postLike);
    }
}
