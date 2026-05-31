package com.rahul.pulse.auth.infrastructure.security;

import com.rahul.pulse.auth.application.usecase.TokenParser;
import com.rahul.pulse.auth.infrastructure.security.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Component
public class JwtTokenParser implements TokenParser {

    private final SecretKey secretKey;

    public JwtTokenParser(JwtProperties jwtProperties, SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public String extractUserId(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    @Override
    public boolean isValid(String token) {

        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);

            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }

    }

}
