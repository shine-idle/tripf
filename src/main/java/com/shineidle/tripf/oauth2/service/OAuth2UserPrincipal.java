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

// TODO : javadoc 작성
@Getter
public class OAuth2UserPrincipal implements OAuth2User, UserDetails {
    private final OAuth2UserInfo userInfo;

    public OAuth2UserPrincipal(OAuth2UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        if (OAuth2Provider.KAKAO.equals(userInfo.getProvider())) {
            return userInfo.getId();
        }
        return userInfo.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return userInfo.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("AUTH_" + UserAuth.NORMAL.getName()));
    }

    @Override
    public String getName() {
        if (OAuth2Provider.KAKAO.equals(userInfo.getProvider())) {
            return userInfo.getId();
        }
        return userInfo.getEmail();
    }

    public OAuth2UserInfo getUserInfo() {
        return userInfo;
    }
}
