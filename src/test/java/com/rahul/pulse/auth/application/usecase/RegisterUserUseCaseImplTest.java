package com.rahul.pulse.auth.application.usecase;


import com.rahul.pulse.auth.application.dto.RegisterUserCommand;
import com.rahul.pulse.auth.application.dto.RegisterUserResult;
import com.rahul.pulse.auth.application.ports.PasswordHasher;
import com.rahul.pulse.auth.domain.exception.UserAlreadyExistsException;
import com.rahul.pulse.auth.domain.model.Email;
import com.rahul.pulse.auth.domain.model.User;
import com.rahul.pulse.auth.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RegisterUserUseCaseImplTest {

    private UserRepository userRepository;
    private PasswordHasher passwordHasher;

    private RegisterUserUseCaseImpl registerUserUseCase;

    @BeforeEach
    void setUp() {

        userRepository = mock(UserRepository.class);

        passwordHasher = mock(PasswordHasher.class);

        registerUserUseCase =
                new RegisterUserUseCaseImpl(
                        userRepository,
                        passwordHasher
                );
    }

    @Test
    void should_register_user_successfully()
    {
        RegisterUserCommand command = new RegisterUserCommand(
                "example@domain.com",
                "example123",
                "John",
                "Doe"
        );

        Email email = new Email(command.email());

        when(userRepository.existsByEmail(email))
                .thenReturn(false);

        when(passwordHasher.encode(command.password()))
                .thenReturn("hashed-password");

        RegisterUserResult result = registerUserUseCase.register(command);

        assertNotNull(result);
        assertEquals("example@domain.com", result.email());


        verify(userRepository).save(any(User.class));

        ArgumentCaptor<User> userCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertEquals(
                "example@domain.com",
                savedUser.getEmail().value()
        );

        assertEquals("hashed-password", savedUser.getPasswordHash().value());

        verify(userRepository, times(1)).existsByEmail(any(Email.class));
        verify(passwordHasher, times(1)).encode(any(String.class));

    }

    @Test
    void should_throw_user_already_exists()
    {
        RegisterUserCommand command = new RegisterUserCommand(
                "example@domain.com",
                "example123",
                "John",
                "Doe"
        );

        Email email = new Email(command.email());

        when(userRepository.existsByEmail(email))
                .thenReturn(true);

        assertThrows(
                UserAlreadyExistsException.class,
                () -> registerUserUseCase.register(command)
        );

        verify(userRepository, never()).save(any(User.class));
        verify(passwordHasher, never()).encode(any(String.class));
    }
}
