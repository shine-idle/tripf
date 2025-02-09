package com.shineidle.tripf.global.security.oauth2.user.google;

import com.shineidle.tripf.global.security.oauth2.user.OAuth2UserUnlink;
import com.shineidle.tripf.global.security.oauth2.user.RestTemplateFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Google OAuth2 계정 연결 해제를 처리하는 클래스입니다.
 * 사용자의 Google OAuth2 계정과 애플리케이션 간의 연결을 해제하는 기능을 제공합니다.
 */
@Component
@RequiredArgsConstructor
public class GoogleOAuth2UserUnlink implements OAuth2UserUnlink {
    private static final String URL = "https://oauth2.googleapis.com/revoke";
    private final RestTemplateFactory restTemplateFactory;

    /**
     * Google OAuth2 계정 연결을 해제합니다.
     *
     * @param accessToken 해제할 OAuth2 액세스 토큰
     */
    @Override
    public void unlink(String accessToken) {
        RestTemplate restTemplate = restTemplateFactory.create();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("token", accessToken);
        restTemplate.postForObject(URL, params, String.class);
    }
}
