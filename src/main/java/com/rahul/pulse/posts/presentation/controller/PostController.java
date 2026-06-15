package com.rahul.pulse.posts.presentation.controller;

import com.rahul.pulse.auth.domain.model.UserId;
import com.rahul.pulse.posts.application.dto.CreatePostCommand;
import com.rahul.pulse.posts.application.dto.CreatePostResult;
import com.rahul.pulse.posts.application.ports.CreatePostUseCase;
import com.rahul.pulse.posts.presentation.dto.CreatePostRequest;
import com.rahul.pulse.posts.presentation.dto.CreatePostResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/posts")
public class PostController {

    final CreatePostUseCase createPostUseCase;

    public PostController(CreatePostUseCase createPostUseCase) {
        this.createPostUseCase = createPostUseCase;
    }

    @PostMapping
    public ResponseEntity<CreatePostResponse> createPost(
            @RequestBody @Valid CreatePostRequest request,
            Authentication authentication
    ){

        String userId = authentication.getName();

        CreatePostCommand command = new CreatePostCommand(
                new UserId(UUID.fromString(userId)),
                request.caption(),
                request.imageUrl()
        );

        CreatePostResult result = createPostUseCase.execute(command);

        return ResponseEntity.ok(
                new CreatePostResponse(
                        result.postId().value().toString()
                )
        );
    }
}
