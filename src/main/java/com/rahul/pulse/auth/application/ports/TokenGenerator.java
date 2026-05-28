package com.rahul.pulse.auth.application.ports;

import com.rahul.pulse.auth.domain.model.User;

public interface TokenGenerator {

    String generateAccessToken(User user);
    String generateRefreshToken(User user);

}
