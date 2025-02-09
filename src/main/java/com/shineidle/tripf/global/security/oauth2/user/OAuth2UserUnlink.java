package com.shineidle.tripf.global.security.oauth2.user;

public interface OAuth2UserUnlink {
    /**
     * OAuth2 계정 연결을 해제합니다.
     */
    void unlink(String accessToken);
}
