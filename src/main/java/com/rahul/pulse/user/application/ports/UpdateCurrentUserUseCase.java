package com.rahul.pulse.user.application.ports;

import com.rahul.pulse.user.application.dto.UpdateCurrentUserCommand;
import com.rahul.pulse.user.application.dto.UpdateCurrentUserResult;

public interface UpdateCurrentUserUseCase {

    UpdateCurrentUserResult execute(UpdateCurrentUserCommand command);

}
