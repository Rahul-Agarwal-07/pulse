package com.rahul.pulse.common.config;

import com.rahul.pulse.auth.application.ports.PasswordHasher;
import com.rahul.pulse.auth.application.ports.RegisterUserUseCase;
import com.rahul.pulse.auth.application.usecase.RegisterUserUseCaseImpl;
import com.rahul.pulse.auth.domain.repository.UserRepository;
import com.rahul.pulse.auth.infrastructure.persistence.repository.UserRepositoryAdapter;
import com.rahul.pulse.auth.infrastructure.security.BCryptPasswordHasher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthBeanConfig {

    @Bean
    public RegisterUserUseCase registerUserUseCase(
        PasswordHasher passwordHasher,
        UserRepository userRepository
    )
    {
        return new RegisterUserUseCaseImpl(userRepository, passwordHasher);
    }

    @Bean
    public PasswordHasher passwordHasher()
    {
        return new BCryptPasswordHasher();
    }

}
