package com.rahul.pulse.auth.application.ports;

import com.rahul.pulse.auth.application.dto.RefreshTokenCommand;
import com.rahul.pulse.auth.application.dto.RefreshTokenResult;


public interface RefreshTokenUseCase {

    RefreshTokenResult refresh(RefreshTokenCommand command);

}
