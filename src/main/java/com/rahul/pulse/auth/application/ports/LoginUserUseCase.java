package com.rahul.pulse.auth.application.ports;

import com.rahul.pulse.auth.application.dto.LoginUserCommand;
import com.rahul.pulse.auth.application.dto.LoginUserResult;

public interface LoginUserUseCase {

    LoginUserResult login(LoginUserCommand command);

}
