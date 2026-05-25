package com.rahul.pulse.auth.presentation.controller;

import com.rahul.pulse.auth.application.dto.RegisterUserCommand;
import com.rahul.pulse.auth.application.dto.RegisterUserResult;
import com.rahul.pulse.auth.application.ports.RegisterUserUseCase;
import com.rahul.pulse.auth.presentation.dto.RegisterRequest;
import com.rahul.pulse.auth.presentation.dto.RegisterResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;

    public AuthController(RegisterUserUseCase registerUserUseCase) {
        this.registerUserUseCase = registerUserUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterRequest request)
    {
        RegisterUserCommand command = new RegisterUserCommand(
                request.email(),
                request.password(),
                request.fullName()
        );

        final RegisterUserResult res = registerUserUseCase.register(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new RegisterResponse(
                        res.userId(),
                        res.email()
                )
        );

    }
}
