package com.shineidle.tripf.oauth2.user.google;

import com.shineidle.tripf.oauth2.user.OAuth2Provider;
import com.shineidle.tripf.oauth2.user.OAuth2UserInfo;

import java.util.Map;

/**
 * Google OAuth2 사용자 정보를 저장하는 클래스입니다.
 * Google의 OAuth2 응답에서 받은 사용자 정보를 파싱하여 제공됩니다.
 */
public class GoogleOAuth2UserInfo implements OAuth2UserInfo {
    private final Map<String, Object> attributes;
    private final String accessToken;
    private final String id;
    private final String email;
    private final String name;
    private final String firstName;
    private final String lastName;
    private final String nickName;
    private final String profileImageUrl;

    /**
     * Google OAuth2 사용자 정보를 생성합니다.
     *
     * @param accessToken Google OAuth2 액세스 토큰
     * @param attributes  Google OAuth2 응답에서 제공하는 사용자 속성 맵
     */
    public GoogleOAuth2UserInfo(String accessToken, Map<String, Object> attributes) {
        this.accessToken = accessToken;
        this.attributes = attributes;
        this.id = (String) attributes.get("sub");
        this.email = (String) attributes.get("email");
        this.name = (String) attributes.get("name");
        this.firstName = (String) attributes.get("given_name");
        this.lastName = (String) attributes.get("family_name");
        this.nickName = null;
        this.profileImageUrl = (String) attributes.get("picture");
    }

    /**
     * OAuth2 제공자를 반환합니다.
     *
     * @return Google OAuth2 제공자 정보
     */
    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.GOOGLE;
    }

    /**
     * OAuth2 액세스 토큰을 반환합니다.
     *
     * @return OAuth2 액세스 토큰
     */
    @Override
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * OAuth2 사용자 속성 맵을 반환합니다.
     *
     * @return 사용자 속성 맵
     */
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /**
     * 사용자의 고유 ID를 반환합니다.
     *
     * @return 사용자 ID
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * 사용자의 이메일을 반환합니다.
     *
     * @return 사용자 이메일
     */
    @Override
    public String getEmail() {
        return email;
    }

    /**
     * 사용자의 전체 이름을 반환합니다.
     *
     * @return 사용자 이름
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * 사용자의 이름(이름 부분)을 반환합니다.
     *
     * @return 사용자 이름
     */
    @Override
    public String getFirstName() {
        return firstName;
    }

    /**
     * 사용자의 성을 반환합니다.
     *
     * @return 사용자 성
     */
    @Override
    public String getLastName() {
        return lastName;
    }

    /**
     * 사용자의 닉네임을 반환합니다.
     *
     * @return 사용자 닉네임 (Google OAuth2에서는 지원되지 않음)
     */
    @Override
    public String getNickname() {
        return nickName;
    }

    /**
     * 사용자의 프로필 이미지 URL을 반환합니다.
     *
     * @return 프로필 이미지 URL
     */
    @Override
    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
