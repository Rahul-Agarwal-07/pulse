package com.rahul.pulse.user.application.usecase;

import com.rahul.pulse.auth.domain.model.User;
import com.rahul.pulse.auth.domain.model.UserId;
import com.rahul.pulse.auth.domain.repository.UserRepository;
import com.rahul.pulse.common.exception.ResourceNotFoundException;
import com.rahul.pulse.user.application.dto.UpdateCurrentUserCommand;
import com.rahul.pulse.user.application.dto.UpdateCurrentUserResult;
import com.rahul.pulse.user.application.ports.UpdateCurrentUserUseCase;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional
public class UpdateCurrentUserUseCaseImpl implements UpdateCurrentUserUseCase {

    final UserRepository userRepository;

    public UpdateCurrentUserUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UpdateCurrentUserResult execute(UpdateCurrentUserCommand command) {

        UserId userId =
                new UserId(UUID.fromString(command.userId()));

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User Not Found"));

        String firstName =
                command.newFirstName() == null ||
                        command.newFirstName().isBlank()
                        ? user.getFirstName()
                        : command.newFirstName();

        String lastName =
                command.newLastName() == null ||
                        command.newLastName().isBlank()
                        ? user.getLastName()
                        : command.newLastName();

        user.updateProfileName(
                firstName,
                lastName
        );

        userRepository.save(user);

        return new UpdateCurrentUserResult(
                "Profile Updated Successfully"
        );
    }
}
