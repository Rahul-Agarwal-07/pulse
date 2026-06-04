package com.rahul.pulse.common.config;

import com.rahul.pulse.auth.application.ports.*;
import com.rahul.pulse.auth.application.usecase.LoginUserUseCaseImpl;
import com.rahul.pulse.auth.application.usecase.RegisterUserUseCaseImpl;
import com.rahul.pulse.auth.domain.repository.RefreshTokenRepository;
import com.rahul.pulse.auth.domain.repository.UserRepository;
import com.rahul.pulse.auth.infrastructure.persistence.repository.UserRepositoryAdapter;
import com.rahul.pulse.auth.infrastructure.security.BCryptPasswordHasher;
import com.rahul.pulse.auth.infrastructure.security.JwtTokenGenerator;
import com.rahul.pulse.auth.infrastructure.security.config.JwtProperties;
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

    @Bean
    public LoginUserUseCase loginUserUseCase(
            UserRepository userRepository,
            TokenGenerator tokenGenerator,
            PasswordHasher passwordHasher,
            RefreshTokenRepository refreshTokenRepository,
            TokenHasher tokenHasher
    ){
        return new LoginUserUseCaseImpl(
                userRepository,
                refreshTokenRepository,
                tokenGenerator,
                passwordHasher,
                tokenHasher
        );
    }
}
