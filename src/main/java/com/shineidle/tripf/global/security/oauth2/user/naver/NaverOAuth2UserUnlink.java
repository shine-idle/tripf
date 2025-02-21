package com.shineidle.tripf.global.security.oauth2.user.naver;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shineidle.tripf.global.security.oauth2.user.OAuth2UserUnlink;
import com.shineidle.tripf.global.security.oauth2.user.RestTemplateFactory;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class NaverOAuth2UserUnlink implements OAuth2UserUnlink {
    private static final String URL = "https://nid.naver.com/oauth2.0/token";

    private final RestTemplateFactory restTemplateFactory;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;

    /**
     * Naver OAuth2 계정 연결을 해제합니다.
     *
     * @param accessToken 해제할 OAuth2 액세스 토큰
     * @throws RuntimeException Naver 계정 해제 실패 시 예외 발생
     */
    @Override
    public void unlink(String accessToken) {
        String url = URL +
                "?service_provider=NAVER" +
                "&grant_type=delete" +
                "&client_id=" +
                clientId +
                "&client_secret=" +
                clientSecret +
                "&access_token=" +
                accessToken;

        RestTemplate restTemplate = restTemplateFactory.create();
        UnlinkResponse response = restTemplate.getForObject(url, UnlinkResponse.class);

        if (response != null && !"success".equalsIgnoreCase(response.getResult())) {
            throw new RuntimeException("Failed to Naver Unlink");
        }
    }

    /**
     * Naver 계정 연결 해제 응답을 저장하는 내부 클래스입니다.
     */
    @Getter
    @RequiredArgsConstructor
    public static class UnlinkResponse {
        @JsonProperty("access_token")
        private final String accessToken;
        private final String result;
    }
}