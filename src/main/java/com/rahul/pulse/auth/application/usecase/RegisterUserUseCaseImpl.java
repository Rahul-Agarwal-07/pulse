package com.rahul.pulse.auth.application.usecase;


import com.rahul.pulse.auth.application.dto.RegisterUserCommand;
import com.rahul.pulse.auth.application.ports.PasswordEncoder;
import com.rahul.pulse.auth.application.ports.RegisterUserUseCase;
import com.rahul.pulse.auth.domain.model.Email;
import com.rahul.pulse.auth.domain.model.PasswordHash;
import com.rahul.pulse.auth.domain.model.User;
import com.rahul.pulse.auth.domain.repository.UserRepository;
import com.rahul.pulse.common.exception.UserAlreadyExistsException;

public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    final UserRepository userRepository;
    final PasswordEncoder passwordEncoder;

    public RegisterUserUseCaseImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(RegisterUserCommand command) {

        Email email = new Email(command.email());

        if(userRepository.existsByEmail(email))
            throw new UserAlreadyExistsException();

        PasswordHash passwordHash = new PasswordHash(command.password());

        User user = User.create(
                email,
                passwordHash,
                command.fullName()
        );

        userRepository.save(user);
    }
}
