package com.rahul.pulse.user.application.usecase;

import com.rahul.pulse.auth.application.ports.TokenParser;
import com.rahul.pulse.auth.domain.exception.InvalidTokenException;
import com.rahul.pulse.auth.domain.model.User;
import com.rahul.pulse.auth.domain.model.UserId;
import com.rahul.pulse.auth.domain.repository.UserRepository;
import com.rahul.pulse.common.exception.ResourceNotFoundException;
import com.rahul.pulse.user.application.dto.GetCurrentUserCommand;
import com.rahul.pulse.user.application.dto.GetCurrentUserResult;
import com.rahul.pulse.user.application.ports.GetCurrentUserUseCase;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional(readOnly = true)
public class GetCurrentUserUseCaseImpl implements GetCurrentUserUseCase {

    final UserRepository userRepository;

    public GetCurrentUserUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public GetCurrentUserResult execute(GetCurrentUserCommand command) {

        UserId userId = new UserId(
                UUID.fromString(command.userId())
        );

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        return new GetCurrentUserResult(
                user.getId().value().toString(),
                user.getEmail().value(),
                user.getFirstName(),
                user.getLastName()
        );
    }
}
