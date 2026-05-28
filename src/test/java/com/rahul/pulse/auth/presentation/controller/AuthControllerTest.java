package com.rahul.pulse.auth.presentation.controller;

import com.rahul.pulse.auth.application.dto.RegisterUserCommand;
import com.rahul.pulse.auth.application.dto.RegisterUserResult;
import com.rahul.pulse.auth.application.ports.RegisterUserUseCase;
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

}
