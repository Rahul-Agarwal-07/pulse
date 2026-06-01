package com.rahul.pulse.auth.infrastructure.security;


import com.rahul.pulse.auth.domain.model.Email;
import com.rahul.pulse.auth.domain.model.PasswordHash;
import com.rahul.pulse.auth.domain.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class JwtTokenIntegrationTest {

    @Autowired
    private JwtTokenGenerator jwtTokenGenerator;

    @Autowired
    private JwtTokenParser jwtTokenParser;

    @Test
    void should_generate_access_token()
    {
        User user = User.create(
                new Email("example@domain.com"),
                new PasswordHash("exampe123"),
                "John",
                "Doe"
        );

        String accessToken = jwtTokenGenerator.generateAccessToken(user);

        assertTrue(jwtTokenParser.isValid(accessToken));

        assertEquals(
                user.getId().value().toString(),
                jwtTokenParser.extractUserId(accessToken)
        );
    }

    @Test
    void should_reject_tampered_token()
    {
        User user = User.create(
                new Email("example@domain.com"),
                new PasswordHash("exampe123"),
                "John",
                "Doe"
        );

        String accessToken = jwtTokenGenerator.generateAccessToken(user);

        assertFalse(jwtTokenParser.isValid(accessToken + "123"));
    }

}
