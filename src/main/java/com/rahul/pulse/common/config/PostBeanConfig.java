package com.rahul.pulse.common.config;

import com.rahul.pulse.posts.application.ports.CreatePostUseCase;
import com.rahul.pulse.posts.application.ports.GetFeedUseCase;
import com.rahul.pulse.posts.application.ports.PostLikeUseCase;
import com.rahul.pulse.posts.application.ports.PostUnlikeUseCase;
import com.rahul.pulse.posts.application.usecase.CreatePostUseCaseImpl;
import com.rahul.pulse.posts.application.usecase.GetFeedUseCaseImpl;
import com.rahul.pulse.posts.application.usecase.PostLikeUseCaseImpl;
import com.rahul.pulse.posts.application.usecase.PostUnlikeUseCaseImpl;
import com.rahul.pulse.posts.domain.repository.FeedRepository;
import com.rahul.pulse.posts.domain.repository.PostLikeRepository;
import com.rahul.pulse.posts.domain.repository.PostRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostBeanConfig {

    @Bean
    public CreatePostUseCase createPostUseCase(
            PostRepository postRepository
    ){
        return new CreatePostUseCaseImpl(postRepository);
    }

    @Bean
    public GetFeedUseCase getFeedUseCase(
            FeedRepository feedRepository
    ){
        return new GetFeedUseCaseImpl(feedRepository);
    }

    @Bean
    public PostLikeUseCase postLikeUseCase(PostLikeRepository postLikeRepository, PostRepository postRepository)
    {
        return new PostLikeUseCaseImpl(postLikeRepository, postRepository);
    }

    @Bean
    public PostUnlikeUseCase postUnlikeUseCase(PostLikeRepository postLikeRepository, PostRepository postRepository)
    {
        return new PostUnlikeUseCaseImpl(postLikeRepository, postRepository);
    }

}
