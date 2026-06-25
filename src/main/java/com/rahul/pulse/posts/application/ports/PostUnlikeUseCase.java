package com.rahul.pulse.posts.application.ports;

import com.rahul.pulse.posts.application.dto.PostUnlikeCommand;

public interface PostUnlikeUseCase {

    void execute(PostUnlikeCommand command);

}
