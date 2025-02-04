package com.shineidle.tripf.oauth2.user.kakao;

import com.shineidle.tripf.oauth2.user.OAuth2UserUnlink;
import com.shineidle.tripf.oauth2.user.RestTemplateFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Kakao OAuth2 계정 연결 해제를 처리하는 클래스입니다.
 * 사용자의 Kakao OAuth2 계정과 애플리케이션 간의 연결을 해제하는 기능을 제공합니다.
 */
@Component
@RequiredArgsConstructor
public class KakaoOAuth2UserUnlink implements OAuth2UserUnlink {
    private static final String URL = "https://kapi.kakao.com/v1/user/unlink";
    private final RestTemplateFactory restTemplateFactory;

    /**
     * Kakao OAuth2 계정 연결을 해제합니다.
     *
     * @param accessToken 해제할 OAuth2 액세스 토큰
     */
    @Override
    public void unlink(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Object> entity = new HttpEntity<>("", headers);

        RestTemplate restTemplate = restTemplateFactory.create();
        restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);
    }
}
