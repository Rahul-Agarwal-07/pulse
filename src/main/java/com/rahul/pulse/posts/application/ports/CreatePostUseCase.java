package com.rahul.pulse.posts.application.ports;

import com.rahul.pulse.posts.application.dto.CreatePostCommand;
import com.rahul.pulse.posts.application.dto.CreatePostResult;

public interface CreatePostUseCase {

    CreatePostResult execute(CreatePostCommand command);

}
