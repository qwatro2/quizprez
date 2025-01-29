package com.quizprez.quizprezauth.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
public class OAuth2Controller {

    @GetMapping("/success")
    public Map<String, Object> success(@AuthenticationPrincipal OAuth2User user) {
        return Map.of(
                "email", Objects.requireNonNull(user.getAttribute("email")),
                "name", Objects.requireNonNull(user.getAttribute("name"))
        );
    }
}

