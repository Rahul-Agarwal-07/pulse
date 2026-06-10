package com.rahul.pulse.users.application.usecase;

import com.rahul.pulse.auth.domain.model.Email;
import com.rahul.pulse.auth.domain.model.PasswordHash;
import com.rahul.pulse.auth.domain.model.User;
import com.rahul.pulse.auth.domain.repository.UserRepository;
import com.rahul.pulse.common.exception.ResourceNotFoundException;
import com.rahul.pulse.user.application.dto.GetCurrentUserCommand;
import com.rahul.pulse.user.application.dto.GetCurrentUserResult;
import com.rahul.pulse.user.application.ports.GetCurrentUserUseCase;
import com.rahul.pulse.user.application.usecase.GetCurrentUserUseCaseImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GetCurrentUserUseCaseTest {

    private UserRepository userRepository;
    private GetCurrentUserUseCase getCurrentUserUseCase;

    @BeforeEach
    void setup()
    {
        userRepository = mock(UserRepository.class);
        getCurrentUserUseCase =  new GetCurrentUserUseCaseImpl(userRepository);
    }

    @Test
    void should_return_user_successfully()
    {
        User user = User.create(
                new Email("example@domain.com"),
                new PasswordHash("hashed-password"),
                "John",
                "Doe"
        );

        GetCurrentUserCommand command = new GetCurrentUserCommand(user.getId().value().toString());

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));

        GetCurrentUserResult result = getCurrentUserUseCase.execute(command);

        assertEquals(user.getId().value().toString(), result.userId());
        assertEquals(user.getEmail().value(), result.email());
        assertEquals(user.getFirstName(), result.firstName());
        assertEquals(user.getLastName(), result.lastName());
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

        GetCurrentUserCommand command = new GetCurrentUserCommand(user.getId().value().toString());

        when(userRepository.findById(user.getId()))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> getCurrentUserUseCase.execute(command)
        );
    }


}
