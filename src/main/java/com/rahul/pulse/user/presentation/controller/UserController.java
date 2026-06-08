package com.rahul.pulse.user.presentation.controller;

import com.rahul.pulse.user.application.dto.GetCurrentUserCommand;
import com.rahul.pulse.user.application.dto.GetCurrentUserResult;
import com.rahul.pulse.user.application.ports.GetCurrentUserUseCase;
import com.rahul.pulse.user.presentation.dto.GetCurrentUserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    final GetCurrentUserUseCase getCurrentUserUseCase;

    public UserController(GetCurrentUserUseCase getCurrentUserUseCase) {
        this.getCurrentUserUseCase = getCurrentUserUseCase;
    }


    @GetMapping("/me")
    public ResponseEntity<GetCurrentUserResponse> getUserProfile(Authentication authentication)
    {
        String userId = authentication.getName();

        GetCurrentUserCommand command = new GetCurrentUserCommand(userId);

        GetCurrentUserResult result = getCurrentUserUseCase.execute(command);

        return ResponseEntity.ok(
                new GetCurrentUserResponse(
                        result.userId(),
                        result.email(),
                        result.firstName(),
                        result.lastName()
                )
        );


    }
}
