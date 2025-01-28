package com.quizprez.quizprezauth.service;

import com.quizprez.quizprezauth.entity.CustomUserDetails;
import com.quizprez.quizprezauth.entity.User;
import com.quizprez.quizprezauth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);

        // Обработка данных Google
        String email = oAuth2User.getAttribute("email");
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setGoogleId(oAuth2User.getAttribute("sub"));
                    newUser.setEnabled(true);
                    return userRepository.save(newUser);
                });

        return new CustomUserDetails(user, oAuth2User.getAttributes());
    }
}
