package com.rahul.pulse.auth.application.ports;

import com.rahul.pulse.auth.application.dto.RegisterUserCommand;
import com.rahul.pulse.auth.application.dto.RegisterUserResult;

public interface RegisterUserUseCase {

    RegisterUserResult register(RegisterUserCommand command);

}
