package com.rahul.pulse.auth.presentation.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class TestProtectedController {

    @GetMapping("/test/protected")
    public String protectedEndpoint(Authentication authentication) {
        return authentication.getName();
    }
}
