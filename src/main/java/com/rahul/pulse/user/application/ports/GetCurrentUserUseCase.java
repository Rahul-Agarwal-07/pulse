package com.rahul.pulse.user.application.ports;

import com.rahul.pulse.user.application.dto.GetCurrentUserCommand;
import com.rahul.pulse.user.application.dto.GetCurrentUserResult;

public interface GetCurrentUserUseCase {

    GetCurrentUserResult execute(GetCurrentUserCommand command);

}
