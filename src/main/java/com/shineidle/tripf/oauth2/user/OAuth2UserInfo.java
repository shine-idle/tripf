package com.shineidle.tripf.oauth2.user;

import java.util.Map;

// TODO : javadoc 작성
public interface OAuth2UserInfo {
    /**
     * OAuth2 제공자를 반환합니다.
     */
    OAuth2Provider getProvider();

    /**
     * OAuth2 액세스 토큰을 반환합니다.
     */
    String getAccessToken();

    /**
     * OAuth2 제공자로부터 받은 사용자 속성 정보를 반환합니다.
     */
    Map<String, Object> getAttributes();

    /**
     * 사용자의 고유 ID를 반환합니다.
     */
    String getId();

    /**
     * 사용자의 이메일을 반환합니다.
     */
    String getEmail();

    /**
     * 사용자의 전체 이름을 반환합니다.
     */
    String getName();

    /**
     * 사용자의 이름(이름 부분)을 반환합니다.
     */
    String getFirstName();

    /**
     * 사용자의 성을 반환합니다.
     */
    String getLastName();

    /**
     * 사용자의 닉네임을 반환합니다.
     */
    String getNickname();

    /**
     * 사용자의 프로필 이미지 URL을 반환합니다.
     */
    String getProfileImageUrl();
}