package com.quizprez.quizprezauth.controller;

import com.quizprez.quizprezauth.entity.CustomOAuth2User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
class OAuth2Controller {
    @GetMapping("/success")
    public Map<String, Object> success(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        SecurityContext contextHolder = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
        Authentication authentication = contextHolder.getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomOAuth2User customUser)) {
            return Map.of("error", "Ошибка аутентификации: пользователь не найден");
        }

        return Map.of(
                "email", customUser.user().getEmail(),
                "accessToken", customUser.accessToken(),
                "refreshToken", customUser.refreshToken()
        );
    }
}
