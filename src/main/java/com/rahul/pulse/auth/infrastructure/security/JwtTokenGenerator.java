package com.rahul.pulse.auth.infrastructure.security;

import com.rahul.pulse.auth.application.ports.TokenGenerator;
import com.rahul.pulse.auth.domain.model.User;
import com.rahul.pulse.auth.infrastructure.security.config.JwtProperties;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenGenerator implements TokenGenerator {

    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    public JwtTokenGenerator(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;

        this.secretKey = Keys.hmacShaKeyFor(
                jwtProperties.secret()
                        .getBytes(StandardCharsets.UTF_8)
        );
    }

    @Override
    public String generateAccessToken(User user) {

        Date now = new Date();

        Date expiry = new Date(
                now.getTime() + jwtProperties.accessTokenExpiration()
        );

        return Jwts.builder()
                .subject(user.getId().value().toString())
                .claim("email", user.getEmail().value())
                .expiration(expiry)
                .issuedAt(now)
                .issuer("pulse")
                .signWith(secretKey)
                .compact();
    }

    @Override
    public String generateRefreshToken(User user) {

        Date now = new Date();

        Date expiry = new Date(
                now.getTime() + jwtProperties.refreshTokenExpiration()
        );

        return Jwts.builder()
                .subject(user.getId().value().toString())
                .expiration(expiry)
                .issuedAt(now)
                .issuer("pulse")
                .signWith(secretKey)
                .compact();
    }
}