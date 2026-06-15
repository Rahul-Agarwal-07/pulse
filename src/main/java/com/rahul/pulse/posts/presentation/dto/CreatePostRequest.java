package com.rahul.pulse.posts.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePostRequest(

        @NotNull @NotBlank
        String caption,

        @NotNull @NotBlank
        String imageUrl
) { }
