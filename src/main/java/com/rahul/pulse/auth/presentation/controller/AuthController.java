package com.rahul.pulse.auth.presentation.controller;

import com.rahul.pulse.auth.application.dto.LoginUserCommand;
import com.rahul.pulse.auth.application.dto.LoginUserResult;
import com.rahul.pulse.auth.application.dto.RegisterUserCommand;
import com.rahul.pulse.auth.application.dto.RegisterUserResult;
import com.rahul.pulse.auth.application.ports.LoginUserUseCase;
import com.rahul.pulse.auth.application.ports.RegisterUserUseCase;
import com.rahul.pulse.auth.presentation.dto.LoginRequest;
import com.rahul.pulse.auth.presentation.dto.LoginResponse;
import com.rahul.pulse.auth.presentation.dto.RegisterRequest;
import com.rahul.pulse.auth.presentation.dto.RegisterResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;

    public AuthController(RegisterUserUseCase registerUserUseCase, LoginUserUseCase loginUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest request)
    {
        RegisterUserCommand command = new RegisterUserCommand(
                request.email(),
                request.password(),
                request.firstName(),
                request.lastName()
        );

        final RegisterUserResult res = registerUserUseCase.register(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new RegisterResponse(
                        res.userId(),
                        res.email()
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request)
    {
        LoginUserCommand command = new LoginUserCommand(
                request.email(),
                request.password()
        );

        final LoginUserResult result = loginUserUseCase.login(command);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        new LoginResponse(
                                result.userId(),
                                result.accessToken(),
                                result.refreshToken()
                        )
                );
    }

    @GetMapping("/me")
    public ResponseEntity<String> me(Authentication authentication) {
        return ResponseEntity.ok(
                authentication.getName()
        );
    }
}
