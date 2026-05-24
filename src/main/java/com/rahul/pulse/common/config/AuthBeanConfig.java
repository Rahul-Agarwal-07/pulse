package com.rahul.pulse.common.config;

import com.rahul.pulse.auth.application.ports.PasswordHasher;
import com.rahul.pulse.auth.application.ports.RegisterUserUseCase;
import com.rahul.pulse.auth.application.usecase.RegisterUserUseCaseImpl;
import com.rahul.pulse.auth.domain.repository.UserRepository;
import org.springframework.context.annotation.Bean;

public class AuthBeanConfig {

    @Bean
    public RegisterUserUseCase registerUserUseCase(
        PasswordHasher passwordHasher,
        UserRepository userRepository
    )
    {
        return new RegisterUserUseCaseImpl(userRepository, passwordHasher);
    }

}
