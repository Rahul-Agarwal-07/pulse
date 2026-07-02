package com.rahul.pulse.posts.presentation.controller;

import com.rahul.pulse.auth.domain.model.UserId;
import com.rahul.pulse.posts.application.dto.*;
import com.rahul.pulse.posts.application.ports.CreatePostUseCase;
import com.rahul.pulse.posts.application.ports.GetFeedUseCase;
import com.rahul.pulse.posts.application.ports.PostLikeUseCase;
import com.rahul.pulse.posts.application.ports.PostUnlikeUseCase;
import com.rahul.pulse.posts.domain.model.PostId;
import com.rahul.pulse.posts.presentation.dto.CreatePostRequest;
import com.rahul.pulse.posts.presentation.dto.CreatePostResponse;
import com.rahul.pulse.posts.presentation.dto.FeedPostResponse;
import com.rahul.pulse.posts.presentation.dto.GetFeedResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/posts")
public class PostController {

    final CreatePostUseCase createPostUseCase;
    final GetFeedUseCase getFeedUseCase;
    final PostLikeUseCase postLikeUseCase;
    final PostUnlikeUseCase postUnlikeUseCase;

    public PostController(CreatePostUseCase createPostUseCase, GetFeedUseCase getFeedUseCase, PostLikeUseCase postLikeUseCase, PostUnlikeUseCase postUnlikeUseCase) {
        this.createPostUseCase = createPostUseCase;
        this.getFeedUseCase = getFeedUseCase;
        this.postLikeUseCase = postLikeUseCase;
        this.postUnlikeUseCase = postUnlikeUseCase;
    }

    @PostMapping("/")
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

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new CreatePostResponse(
                        result.postId().value().toString()
                )
        );
    }

    @GetMapping("/feed")
    public ResponseEntity<GetFeedResponse> getFeed(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        UserId currentUser = new UserId(UUID.fromString(authentication.getName()));

        GetFeedCommand command = new GetFeedCommand(
                currentUser,
                page,
                size
        );

        GetFeedResult result = getFeedUseCase.execute(command);

        return ResponseEntity.ok(
                new GetFeedResponse(
                        result.content()
                                .stream()
                                .map(FeedPostResponse::from)
                                .toList()
                )
        );
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Void> postLike(
            @PathVariable String postId,
            Authentication authentication
    )
    {
        PostLikeCommand command = new PostLikeCommand(
                new UserId(UUID.fromString(authentication.getName())),
                new PostId(UUID.fromString(postId))
        );

        postLikeUseCase.execute(command);

        return ResponseEntity.ok(null);
    }

    @DeleteMapping("/{postId}/like")
    public ResponseEntity<Void> postUnlike(
            @PathVariable String postId,
            Authentication authentication
    )
    {
        PostUnlikeCommand command = new PostUnlikeCommand(
                new UserId(UUID.fromString(authentication.getName())),
                new PostId(UUID.fromString(postId))
        );

        postUnlikeUseCase.execute(command);

        return ResponseEntity.ok(null);
    }
}
