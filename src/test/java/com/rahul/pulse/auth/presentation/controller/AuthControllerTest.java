package com.rahul.pulse.auth.presentation.controller;

import com.rahul.pulse.auth.application.dto.LoginUserCommand;
import com.rahul.pulse.auth.application.dto.LoginUserResult;
import com.rahul.pulse.auth.application.dto.RegisterUserCommand;
import com.rahul.pulse.auth.application.dto.RegisterUserResult;
import com.rahul.pulse.auth.application.ports.LoginUserUseCase;
import com.rahul.pulse.auth.application.ports.RegisterUserUseCase;
import com.rahul.pulse.auth.domain.exception.InvalidCredentialsException;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RegisterUserUseCase registerUserUseCase;

    @MockitoBean
    private LoginUserUseCase loginUserUseCase;

    @Test
    void should_register_user_successfully() throws Exception
    {
        RegisterUserResult result = new RegisterUserResult(
                UUID.randomUUID().toString(),
                "example@domain.com"
        );

        when(registerUserUseCase.register(any(RegisterUserCommand.class)))
                .thenReturn(result);

        String requestBody = """
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
                        .content(requestBody)
        )
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.email").value("example@domain.com")
                );
    }

    @Test
    void should_login_user_successfully() throws Exception
    {
        LoginUserResult result = new LoginUserResult(
                UUID.randomUUID().toString(),
                "accessToken",
                "refreshToken"
        );

        when(loginUserUseCase.login(any(LoginUserCommand.class)))
                .thenReturn(result);

        String requestBody = """
                {
                    "email":"example@domain.com",
                    "password":"password123"
                }
                """;

        mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("accessToken"))
                .andExpect(jsonPath("$.refreshToken").value("refreshToken"));
    }

    @Test
    void should_return_401_when_credentials_invalid() throws Exception
    {
        when(loginUserUseCase.login(any(LoginUserCommand.class)))
                .thenThrow(InvalidCredentialsException.class);

        String requestBody = """
                {
                    "email":"example@domain.com",
                    "password":"password123"
                }
                """;

        mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isUnauthorized());
    }

}
