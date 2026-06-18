package com.rahul.pulse.post.presentation.controller;

import com.rahul.pulse.auth.infrastructure.security.JwtAuthenticationFilter;
import com.rahul.pulse.posts.application.dto.CreatePostCommand;
import com.rahul.pulse.posts.application.dto.CreatePostResult;
import com.rahul.pulse.posts.application.ports.CreatePostUseCase;
import com.rahul.pulse.posts.domain.model.PostId;
import com.rahul.pulse.posts.presentation.controller.PostController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = PostController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
public class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private CreatePostUseCase createPostUseCase;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void should_create_post_end_to_end() throws Exception {
        CreatePostResult result = new CreatePostResult(
                new PostId(UUID.randomUUID())
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(UUID.randomUUID(), null);

        when(createPostUseCase.execute(any(CreatePostCommand.class)))
                .thenReturn(result);

        String requestBody = """
                {
                    "caption" : "this is test caption",
                    "imageUrl" : "https://testimage.com"
                }
                """;

        mockMvc.perform(
                post("/posts/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .principal(authentication)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.postId").value(result.postId().value().toString()));
    }

}
