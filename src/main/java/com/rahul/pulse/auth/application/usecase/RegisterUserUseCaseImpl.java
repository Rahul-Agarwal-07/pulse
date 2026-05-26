package com.rahul.pulse.auth.application.usecase;
import com.rahul.pulse.auth.application.dto.RegisterUserCommand;
import com.rahul.pulse.auth.application.dto.RegisterUserResult;
import com.rahul.pulse.auth.application.ports.PasswordHasher;
import com.rahul.pulse.auth.application.ports.RegisterUserUseCase;
import com.rahul.pulse.auth.domain.model.Email;
import com.rahul.pulse.auth.domain.model.PasswordHash;
import com.rahul.pulse.auth.domain.model.User;
import com.rahul.pulse.auth.domain.repository.UserRepository;
import com.rahul.pulse.auth.domain.exception.UserAlreadyExistsException;

public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    final UserRepository userRepository;
    final PasswordHasher passwordHasher;

    public RegisterUserUseCaseImpl(UserRepository userRepository, PasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public RegisterUserResult register(RegisterUserCommand command) {

        Email email = new Email(command.email());

        if(userRepository.existsByEmail(email))
            throw new UserAlreadyExistsException();

        String hash = passwordHasher.encode(command.password());
        PasswordHash passwordHash = new PasswordHash(hash);

        User user = User.create(
                email,
                passwordHash,
                command.firstName(),
                command.lastName()
        );

        userRepository.save(user);

        return new RegisterUserResult(
                user.getId().value().toString(),
                user.getEmail().value()
        );
    }
}
