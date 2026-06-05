package com.rahul.pulse.auth.presentation.controller;

import com.rahul.pulse.auth.application.dto.*;
import com.rahul.pulse.auth.application.ports.LoginUserUseCase;
import com.rahul.pulse.auth.application.ports.RefreshTokenUseCase;
import com.rahul.pulse.auth.application.ports.RegisterUserUseCase;
import com.rahul.pulse.auth.presentation.dto.*;
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
    private final RefreshTokenUseCase refreshTokenUseCase;

    public AuthController(RegisterUserUseCase registerUserUseCase, LoginUserUseCase loginUserUseCase, RefreshTokenUseCase refreshTokenUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
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

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(
            @RequestHeader("Refresh-Token") String refreshToken
    )
    {
        System.out.println("Controller reached");

        RefreshTokenCommand command = new RefreshTokenCommand(refreshToken);

        final RefreshTokenResult result = refreshTokenUseCase.refresh(command);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(
                        new RefreshResponse(
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
