package com.rahul.pulse.auth.infrastructure.security;

import com.rahul.pulse.auth.domain.model.Email;
import com.rahul.pulse.auth.domain.model.PasswordHash;
import com.rahul.pulse.auth.domain.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class JwtAuthenticationFilterTest {

    @Autowired
    private JwtTokenParser parser;

    @Autowired
    private JwtTokenGenerator generator;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_authenticate_user_with_valid_token() throws Exception
    {
        User user = User.create(
                new Email("example@domain.com"),
                new PasswordHash("exampe123"),
                "John",
                "Doe"
        );

        String accessToken = generator.generateAccessToken(user);

        mockMvc.perform(
                get("/test/protected")
                        .header("Authorization", "Bearer " + accessToken)
        )
                .andExpect(status().isOk())
                .andExpect(
                        content().string(
                                user.getId().value().toString()
                        )
                );
    }

    @Test
    void should_reject_request_when_token_missing() throws Exception
    {
        mockMvc.perform(
                        get("/test/protected")
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void should_reject_request_when_token_tampered() throws Exception
    {
        User user = User.create(
                new Email("example@domain.com"),
                new PasswordHash("exampe123"),
                "John",
                "Doe"
        );

        String accessToken = generator.generateAccessToken(user) + "abc";

        mockMvc.perform(
                        get("/test/protected")
                                .header("Authorization", "Bearer " + accessToken)
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void should_reject_request_when_authorization_header_not_bearer()
            throws Exception {

        User user = User.create(
                new Email("example@domain.com"),
                new PasswordHash("example123"),
                "John",
                "Doe"
        );

        String token = generator.generateAccessToken(user);

        mockMvc.perform(
                        get("/test/protected")
                                .header("Authorization", "Basic " + token)
                )
                .andExpect(status().isForbidden());
    }


}
