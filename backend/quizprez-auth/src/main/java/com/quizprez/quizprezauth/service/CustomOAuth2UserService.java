package com.quizprez.quizprezauth.service;

import com.quizprez.quizprezauth.entity.CustomOAuth2User;
import com.quizprez.quizprezauth.entity.RefreshToken;
import com.quizprez.quizprezauth.entity.User;
import com.quizprez.quizprezauth.exception.OAuth2AuthenticationProcessingException;
import com.quizprez.quizprezauth.oauth2.OAuth2UserInfo;
import com.quizprez.quizprezauth.oauth2.OAuth2UserInfoFactory;
import com.quizprez.quizprezauth.repository.UserRepository;
import com.quizprez.quizprezauth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);

        String registrationId = request.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());

        String email = oAuth2UserInfo.getEmail();
        if (email == null) {
            throw new OAuth2AuthenticationProcessingException("Не удалось получить email пользователя");
        }

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setGoogleId(oAuth2UserInfo.getId());
                    newUser.setEnabled(true);
                    return userRepository.save(newUser);
                });

        String accessToken = jwtUtil.generateAccessToken(email);

        RefreshToken refreshTokenEntity = refreshTokenService.createRefreshToken(user);
        String refreshToken = refreshTokenEntity.getToken();

        CustomOAuth2User customUser = new CustomOAuth2User(user, accessToken, refreshToken, oAuth2UserInfo.getAttributes());

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(customUser, null, customUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return customUser;
    }
}
