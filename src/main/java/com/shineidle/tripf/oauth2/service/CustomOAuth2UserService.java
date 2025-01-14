package com.shineidle.tripf.oauth2.service;

import com.shineidle.tripf.oauth2.exception.OAuth2AuthenticationProcessingException;
import com.shineidle.tripf.oauth2.user.OAuth2Provider;
import com.shineidle.tripf.oauth2.user.OAuth2UserInfo;
import com.shineidle.tripf.oauth2.user.OAuth2UserInfoFactory;
import com.shineidle.tripf.user.entity.User;
import com.shineidle.tripf.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        return processOAuth2User(oAuth2UserRequest, oAuth2User);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // Google, Kakao, Naver
        String accessToken = userRequest.getAccessToken().getTokenValue();

        log.info("소셜 로그인 플랫폼: {}", registrationId);
        log.info("소셜 로그인 엑세스 토큰: {}", accessToken);

        // OAuth2UserInfo 객체 생성
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                registrationId,
                accessToken,
                oAuth2User.getAttributes()
        );

        // 이메일 검증
        if (!StringUtils.hasText(oAuth2UserInfo.getEmail()) && !OAuth2Provider.KAKAO.getRegistrationId().equals(registrationId)) {
            throw new OAuth2AuthenticationProcessingException("이메일을 찾을 수 없습니다.");
        }

        // 유저 정보 저장 또는 업데이트
        saveOrUpdate(oAuth2UserInfo);

        // OAuth2UserPrincipal 반환
        return new OAuth2UserPrincipal(oAuth2UserInfo);
    }

    private void saveOrUpdate(OAuth2UserInfo oAuth2UserInfo) {
        Optional<User> existUser = userRepository.findByEmail(oAuth2UserInfo.getEmail());

        if (existUser.isEmpty() && OAuth2Provider.KAKAO.equals(oAuth2UserInfo.getProvider())) {
            existUser = userRepository.findByProviderId(oAuth2UserInfo.getId());
        }

        if (existUser.isPresent()) {
            // 기존 유저 정보 업데이트
            User user = existUser.get();
            user.update(oAuth2UserInfo);
        } else {
            User newUser;

            if (OAuth2Provider.KAKAO.equals(oAuth2UserInfo.getProvider())) {
                newUser = new User(null,
                        oAuth2UserInfo.getNickname(),
                        oAuth2UserInfo.getProvider().getRegistrationId(),
                        oAuth2UserInfo.getId()
                );
            } else {
                newUser = new User(oAuth2UserInfo.getEmail(),
                        oAuth2UserInfo.getName(),
                        oAuth2UserInfo.getProvider().getRegistrationId(),
                        oAuth2UserInfo.getId()
                        );
            }
            // 새로운 유저 생성
            userRepository.save(newUser);
        }
    }
}
