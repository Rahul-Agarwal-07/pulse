package com.rahul.pulse.auth.infrastructure.security;

import com.rahul.pulse.auth.application.ports.TokenParser;
import com.rahul.pulse.auth.infrastructure.security.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

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

    @Override
    public boolean isRefreshToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return claims.get("type") == "refresh";
        }catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

}
