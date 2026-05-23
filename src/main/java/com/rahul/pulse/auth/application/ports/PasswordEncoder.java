package com.rahul.pulse.auth.application.ports;

import com.rahul.pulse.auth.domain.model.PasswordHash;

public interface PasswordEncoder {
    PasswordHash encode(String password);
}
