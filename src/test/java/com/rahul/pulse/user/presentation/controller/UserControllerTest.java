package com.rahul.pulse.user.presentation.controller;

import com.rahul.pulse.auth.infrastructure.security.JwtAuthenticationFilter;
import com.rahul.pulse.user.application.dto.GetCurrentUserCommand;
import com.rahul.pulse.user.application.dto.GetCurrentUserResult;
import com.rahul.pulse.user.application.dto.UpdateCurrentUserCommand;
import com.rahul.pulse.user.application.dto.UpdateCurrentUserResult;
import com.rahul.pulse.user.application.ports.GetCurrentUserUseCase;
import com.rahul.pulse.user.application.ports.UpdateCurrentUserUseCase;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = UserController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class
        }
)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private GetCurrentUserUseCase getCurrentUserUseCase;

    @MockitoBean
    private UpdateCurrentUserUseCase updateCurrentUserUseCase;

    @Test
    void should_return_user_profile_successfully() throws Exception {

        GetCurrentUserResult result =
                new GetCurrentUserResult(
                        UUID.randomUUID().toString(),
                        "example@domain.com",
                        "John",
                        "Doe"
                );

        Authentication auth =
                new UsernamePasswordAuthenticationToken(
                        result.userId(),
                        null
                );

        when(getCurrentUserUseCase.execute(any(GetCurrentUserCommand.class)))
                .thenReturn(result);

        mockMvc.perform(
                    get("/users/me")
                            .principal(auth)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email")
                        .value("example@domain.com"))
                .andExpect(jsonPath("$.firstName")
                        .value("John"))
                .andExpect(jsonPath("$.lastName")
                        .value("Doe"));
    }

    @Test
    void should_update_user_successfully() throws Exception
    {
        UpdateCurrentUserResult result = new UpdateCurrentUserResult(
                "Profile Updated Successfully"
        );

        when(updateCurrentUserUseCase.execute(any(UpdateCurrentUserCommand.class)))
                .thenReturn(result);

        Authentication auth =
                new UsernamePasswordAuthenticationToken(
                        UUID.randomUUID().toString(),
                        null
                );

        String requestBody =  """
                                    {
                                        "firstName" : "John",
                                        "lastName" : "Doe"       
                                    }
                                """;

        mockMvc.perform(
                patch("/users/me")
                        .principal(auth)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }
}