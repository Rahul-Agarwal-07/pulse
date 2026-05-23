package com.rahul.pulse.auth.application.ports;

import com.rahul.pulse.auth.application.dto.RegisterUserCommand;

public interface RegisterUserUseCase {

    void register(RegisterUserCommand command);

}
