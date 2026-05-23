package com.rahul.pulse.auth.domain.repository;

import com.rahul.pulse.auth.domain.model.Email;
import com.rahul.pulse.auth.domain.model.User;
import com.rahul.pulse.auth.domain.model.UserId;

public interface UserRepository {

    boolean existsById(UserId userId);
    boolean existsByEmail(Email email);
    boolean save(User user);

}
