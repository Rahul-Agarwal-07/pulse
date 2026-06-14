package com.rahul.pulse.user.application.usecase;

import com.rahul.pulse.auth.domain.model.Email;
import com.rahul.pulse.auth.domain.model.PasswordHash;
import com.rahul.pulse.auth.domain.model.User;
import com.rahul.pulse.auth.domain.repository.UserRepository;
import com.rahul.pulse.common.exception.ResourceNotFoundException;
import com.rahul.pulse.user.application.dto.UpdateCurrentUserCommand;
import com.rahul.pulse.user.application.ports.UpdateCurrentUserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UpdateCurrentUserUseCaseTest {

    private UserRepository userRepository;
    private UpdateCurrentUserUseCase updateCurrentUserUseCase;

    @BeforeEach
    void setup()
    {
        userRepository = mock(UserRepository.class);
        updateCurrentUserUseCase = new UpdateCurrentUserUseCaseImpl(userRepository);
    }

    @Test
    void should_update_profile_successfully()
    {
        User user = User.create(
                new Email("example@domain.com"),
                new PasswordHash("hashed-password"),
                "John",
                "Doe"
        );

        UpdateCurrentUserCommand command = new UpdateCurrentUserCommand(
                user.getId().value().toString(),
                "Michael",
                "John"
        );

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        updateCurrentUserUseCase.execute(command);

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(argumentCaptor.capture());

        User saved = argumentCaptor.getValue();

        assertEquals(command.newFirstName(), saved.getFirstName());
        assertEquals(command.newLastName(), saved.getLastName());
    }

    @Test
    void should_throw_when_user_not_found()
    {
        User user = User.create(
                new Email("example@domain.com"),
                new PasswordHash("hashed-password"),
                "John",
                "Doe"
        );

        UpdateCurrentUserCommand command = new UpdateCurrentUserCommand(
                user.getId().value().toString(),
                "Michael",
                "John"
        );

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> updateCurrentUserUseCase.execute(command)
        );

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void should_keep_existing_names_if_fields_are_blank()
    {
        User user = User.create(
                new Email("example@domain.com"),
                new PasswordHash("hashed-password"),
                "John",
                "Doe"
        );

        UpdateCurrentUserCommand command = new UpdateCurrentUserCommand(
                user.getId().value().toString(),
                "Michael",
                ""
        );

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        String oldLastName = user.getLastName();

        updateCurrentUserUseCase.execute(command);

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(argumentCaptor.capture());

        User saved = argumentCaptor.getValue();

        assertEquals(command.newFirstName(), saved.getFirstName());
        assertEquals(oldLastName, saved.getLastName());
    }

}
