package com.rahul.pulse.user.presentation.controller;

import com.rahul.pulse.user.application.dto.GetCurrentUserCommand;
import com.rahul.pulse.user.application.dto.GetCurrentUserResult;
import com.rahul.pulse.user.application.dto.UpdateCurrentUserCommand;
import com.rahul.pulse.user.application.dto.UpdateCurrentUserResult;
import com.rahul.pulse.user.application.ports.GetCurrentUserUseCase;
import com.rahul.pulse.user.application.ports.UpdateCurrentUserUseCase;
import com.rahul.pulse.user.presentation.dto.GetCurrentUserResponse;
import com.rahul.pulse.user.presentation.dto.UpdateCurrentUserRequest;
import com.rahul.pulse.user.presentation.dto.UpdateCurrentUserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    final GetCurrentUserUseCase getCurrentUserUseCase;
    final UpdateCurrentUserUseCase updateCurrentUserUseCase;

    public UserController(GetCurrentUserUseCase getCurrentUserUseCase, UpdateCurrentUserUseCase updateCurrentUserUseCase) {
        this.getCurrentUserUseCase = getCurrentUserUseCase;
        this.updateCurrentUserUseCase = updateCurrentUserUseCase;
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

    @PatchMapping("/me")
    public ResponseEntity<UpdateCurrentUserResponse> updatedUserProfile(
            Authentication authentication, @RequestBody UpdateCurrentUserRequest request
    ){
        String userId = authentication.getName();

        UpdateCurrentUserCommand command = new UpdateCurrentUserCommand(
                userId,
                request.firstName(),
                request.lastName()
        );

        UpdateCurrentUserResult result = updateCurrentUserUseCase.execute(command);

        return ResponseEntity.ok(
                new UpdateCurrentUserResponse(result.message())
        );
    }
}
