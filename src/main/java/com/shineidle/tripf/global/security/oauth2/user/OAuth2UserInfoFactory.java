package com.shineidle.tripf.global.security.oauth2.user;

import com.shineidle.tripf.global.security.oauth2.exception.OAuth2AuthenticationProcessingException;
import com.shineidle.tripf.global.security.oauth2.user.google.GoogleOAuth2UserInfo;
import com.shineidle.tripf.global.security.oauth2.user.kakao.KakaoOAuth2UserInfo;
import com.shineidle.tripf.global.security.oauth2.user.naver.NaverOAuth2UserInfo;

import java.util.Map;

public class OAuth2UserInfoFactory {

    /**
     * OAuth2 제공자(registrationId)에 따라 적절한 OAuth2UserInfo 객체를 반환합니다.
     *
     * @param registrationId OAuth2 제공자 식별자 (Google, Naver, Kakao 등)
     * @param accessToken    OAuth2 액세스 토큰
     * @param attributes     OAuth2 제공자로부터 받은 사용자 속성 정보
     * @return OAuth2UserInfo 객체 (Google, Naver, Kakao 중 하나)
     * @throws OAuth2AuthenticationProcessingException 지원하지 않는 제공자일 경우 예외 발생
     */
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, String accessToken, Map<String, Object> attributes) {
        if (OAuth2Provider.GOOGLE.getRegistrationId().equals(registrationId)) {
            return new GoogleOAuth2UserInfo(accessToken, attributes);
        } else if (OAuth2Provider.NAVER.getRegistrationId().equals(registrationId)) {
            return new NaverOAuth2UserInfo(accessToken, attributes);
        } else if (OAuth2Provider.KAKAO.getRegistrationId().equals(registrationId)) {
            return new KakaoOAuth2UserInfo(accessToken, attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Login with " + registrationId + " is not supported");
        }
    }
}
