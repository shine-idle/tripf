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

/**
 * OAuth2 인증 과정에서 사용자 정보를 처리하는 서비스 클래스입니다.
 * 사용자의 OAuth2 로그인 정보를 기반으로 새로운 사용자 등록 또는 기존 사용자 정보를 갱신합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    /**
     * OAuth2 사용자 정보를 로드합니다.
     *
     * @param oAuth2UserRequest OAuth2 사용자 요청 객체
     * @return OAuth2User 객체
     * @throws OAuth2AuthenticationException 인증 예외 발생 시
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        return processOAuth2User(oAuth2UserRequest, oAuth2User);
    }

    /**
     * OAuth2 사용자 정보를 처리하여 OAuth2UserPrincipal을 반환합니다.
     *
     * @param userRequest OAuth2 사용자 요청 객체
     * @param oAuth2User  OAuth2 사용자 객체
     * @return OAuth2UserPrincipal 객체
     */
    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String accessToken = userRequest.getAccessToken().getTokenValue();

        log.info("소셜 로그인 플랫폼: {}", registrationId);
        log.info("소셜 로그인 엑세스 토큰: {}", accessToken);

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                registrationId,
                accessToken,
                oAuth2User.getAttributes()
        );

        if (!StringUtils.hasText(oAuth2UserInfo.getEmail()) && !OAuth2Provider.KAKAO.getRegistrationId().equals(registrationId)) {
            throw new OAuth2AuthenticationProcessingException("이메일을 찾을 수 없습니다.");
        }

        saveOrUpdate(oAuth2UserInfo);

        return new OAuth2UserPrincipal(oAuth2UserInfo);
    }

    /**
     * OAuth2 사용자 정보를 저장하거나 기존 정보를 업데이트합니다.
     *
     * @param oAuth2UserInfo OAuth2 사용자 정보 객체
     */
    private void saveOrUpdate(OAuth2UserInfo oAuth2UserInfo) {
        Optional<User> existUser = userRepository.findByEmail(oAuth2UserInfo.getEmail());

        if (existUser.isEmpty() && OAuth2Provider.KAKAO.equals(oAuth2UserInfo.getProvider())) {
            existUser = userRepository.findByProviderId(oAuth2UserInfo.getId());
        }

        if (existUser.isPresent()) {
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
            userRepository.save(newUser);
        }
    }
}
