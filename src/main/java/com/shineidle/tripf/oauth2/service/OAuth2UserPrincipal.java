package com.shineidle.tripf.oauth2.service;

import com.shineidle.tripf.oauth2.user.OAuth2Provider;
import com.shineidle.tripf.oauth2.user.OAuth2UserInfo;
import com.shineidle.tripf.user.type.UserAuth;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * OAuth2 인증된 사용자 정보를 나타내는 클래스입니다.
 * OAuth2User 및 UserDetails 인터페이스를 구현하여 인증 및 사용자 정보를 관리합니다.
 */
@Getter
public class OAuth2UserPrincipal implements OAuth2User, UserDetails {
    private final OAuth2UserInfo userInfo;

    /**
     * OAuth2 사용자 정보를 기반으로 객체를 생성합니다.
     *
     * @param userInfo OAuth2 사용자 정보 객체
     */
    public OAuth2UserPrincipal(OAuth2UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    /**
     * OAuth2 인증은 비밀번호를 사용하지 않으므로 null을 반환합니다.
     *
     * @return null (비밀번호 없음)
     */
    @Override
    public String getPassword() {
        return null;
    }

    /**
     * 사용자의 식별자로 이메일 또는 소셜 로그인 ID를 반환합니다.
     *
     * @return 사용자 이름 (카카오의 경우 ID, 그 외에는 이메일)
     */
    @Override
    public String getUsername() {
        if (OAuth2Provider.KAKAO.equals(userInfo.getProvider())) {
            return userInfo.getId();
        }
        return userInfo.getEmail();
    }

    /**
     * 계정이 만료되지 않았음을 나타냅니다.
     *
     * @return 항상 true (만료되지 않음)
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정이 잠기지 않았음을 나타냅니다.
     *
     * @return 항상 true (잠금되지 않음)
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 자격 증명이 만료되지 않았음을 나타냅니다.
     *
     * @return 항상 true (만료되지 않음)
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정이 활성화되었음을 나타냅니다.
     *
     * @return 항상 true (활성화 상태)
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * OAuth2 사용자 속성을 반환합니다.
     *
     * @return OAuth2 사용자 속성 맵
     */
    @Override
    public Map<String, Object> getAttributes() {
        return userInfo.getAttributes();
    }

    /**
     * 사용자의 권한을 반환합니다.
     *
     * @return 기본 권한을 가진 GrantedAuthority 컬렉션
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("AUTH_" + UserAuth.NORMAL.getName()));
    }

    /**
     * OAuth2 사용자 이름을 반환합니다.
     *
     * @return 사용자 이름 (카카오의 경우 ID, 그 외에는 이메일)
     */
    @Override
    public String getName() {
        if (OAuth2Provider.KAKAO.equals(userInfo.getProvider())) {
            return userInfo.getId();
        }
        return userInfo.getEmail();
    }

    /**
     * OAuth2 사용자 정보를 반환합니다.
     *
     * @return OAuth2UserInfo 객체
     */
    public OAuth2UserInfo getUserInfo() {
        return userInfo;
    }
}
