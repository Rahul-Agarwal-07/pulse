package com.rahul.pulse.auth.shared;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rahul.pulse.auth.application.dto.LoginUserCommand;
import com.rahul.pulse.auth.application.dto.LoginUserResult;
import com.rahul.pulse.auth.application.dto.RegisterUserCommand;
import com.rahul.pulse.auth.application.dto.RegisterUserResult;
import com.rahul.pulse.auth.application.ports.LoginUserUseCase;
import com.rahul.pulse.auth.application.ports.RefreshTokenUseCase;
import com.rahul.pulse.auth.application.ports.RegisterUserUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@Transactional
public class CompleteIntegrationTest {

    @ServiceConnection
    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17")
                    .withDatabaseName("pulse_test")
                    .withUsername("postgres")
                    .withPassword("postgres");

    @Autowired
    private RegisterUserUseCase registerUserUseCase;

    @Autowired
    private LoginUserUseCase loginUserUseCase;

    @Autowired
    private RefreshTokenUseCase refreshTokenUseCase;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void end_to_end_auth_testing() throws Exception {

        String registerRequestBody = """
                {
                    "email":"example@domain.com",
                    "password":"password123",
                    "firstName":"John",
                    "lastName":"Doe"
                }
                """;

        mockMvc.perform(
                        post("/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(registerRequestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.email").value("example@domain.com")
                );

        String loginRequestBody = """
                {
                    "email":"example@domain.com",
                    "password":"password123"
                }
                """;

        MvcResult loginMvcResult =
                mockMvc.perform(
                                post("/auth/login")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(loginRequestBody)
                        )
                        .andExpect(status().isOk())
                        .andReturn();

        JsonNode loginJson =
                objectMapper.readTree(
                        loginMvcResult.getResponse().getContentAsString()
                );

        String accessToken =
                loginJson.get("accessToken").asText();

        String refreshToken =
                loginJson.get("refreshToken").asText();


        // Test protected endpoint

        mockMvc.perform(
                        get("/test/protected")
                                .header(
                                        "Authorization",
                                        "Bearer " + accessToken
                                )
                )
                .andExpect(status().isOk());


        // Test refresh flow

        MvcResult refreshResult =
                mockMvc.perform(
                                post("/auth/refresh")
                                        .header(
                                                "Refresh-Token",
                                                refreshToken
                                        )
                        )
                        .andExpect(status().isOk())
                        .andReturn();

        JsonNode refreshJson =
                objectMapper.readTree(
                        refreshResult.getResponse().getContentAsString()
                );

        String newAccessToken =
                refreshJson.get("accessToken").asText();

        String newRefreshToken =
                refreshJson.get("refreshToken").asText();

        assertNotEquals(accessToken, newAccessToken);
        assertNotEquals(refreshToken, newRefreshToken);

        // =========================
        // New Access Token Works
        // =========================

        mockMvc.perform(
                        get("/test/protected")
                                .header(
                                        "Authorization",
                                        "Bearer " + newAccessToken
                                )
                )
                .andExpect(status().isOk());

        // Reuse detection

        mockMvc.perform(
                        post("/auth/refresh")
                                .header(
                                        "Refresh-Token",
                                        refreshToken
                                )
                )
                .andExpect(status().isUnauthorized());
    }

}
