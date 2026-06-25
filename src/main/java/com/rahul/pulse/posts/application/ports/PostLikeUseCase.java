package com.rahul.pulse.posts.application.ports;

import com.rahul.pulse.posts.application.dto.PostLikeCommand;

public interface PostLikeUseCase {

    void execute(PostLikeCommand command);

}
