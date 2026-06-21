package com.rahul.pulse.common.config;

import com.rahul.pulse.posts.application.ports.CreatePostUseCase;
import com.rahul.pulse.posts.application.ports.GetFeedUseCase;
import com.rahul.pulse.posts.application.usecase.CreatePostUseCaseImpl;
import com.rahul.pulse.posts.application.usecase.GetFeedUseCaseImpl;
import com.rahul.pulse.posts.domain.repository.FeedRepository;
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

}
