package com.rahul.pulse.posts.application.ports;

import com.rahul.pulse.posts.application.dto.GetFeedCommand;
import com.rahul.pulse.posts.application.dto.GetFeedResult;

public interface GetFeedUseCase {

    GetFeedResult execute(GetFeedCommand command);

}
