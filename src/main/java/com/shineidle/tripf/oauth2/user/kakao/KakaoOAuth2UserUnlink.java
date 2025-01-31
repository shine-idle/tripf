package com.shineidle.tripf.oauth2.user.kakao;

import com.shineidle.tripf.oauth2.user.OAuth2UserUnlink;
import com.shineidle.tripf.oauth2.user.RestTemplateFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KakaoOAuth2UserUnlink implements OAuth2UserUnlink {
    private static final String URL = "https://kapi.kakao.com/v1/user/unlink";
    private final RestTemplateFactory restTemplateFactory;

    @Override
    public void unlink(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Object> entity = new HttpEntity<>("", headers);

        RestTemplate restTemplate = restTemplateFactory.create();
        restTemplate.exchange(URL, HttpMethod.POST, entity, String.class);
    }
}
