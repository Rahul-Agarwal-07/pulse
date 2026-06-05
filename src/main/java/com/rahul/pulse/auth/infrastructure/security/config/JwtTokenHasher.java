package com.rahul.pulse.auth.infrastructure.security.config;

import com.rahul.pulse.auth.application.ports.TokenHasher;
import io.jsonwebtoken.security.HashAlgorithm;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class JwtTokenHasher implements TokenHasher {

    public static String sha256(String token){
        try {

            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(token.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();

            for (byte b : hash)
            {
                hexString.append(String.format("%02x", b));
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String hash(String token) {
        return sha256(token);
    }
}
