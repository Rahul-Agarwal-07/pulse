package com.rahul.pulse.auth.application.ports;

import com.rahul.pulse.auth.application.dto.LogoutCommand;

public interface LogoutUseCase {

    void logout(LogoutCommand command);

}
