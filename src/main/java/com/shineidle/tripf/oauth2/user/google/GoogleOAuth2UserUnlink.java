package com.shineidle.tripf.oauth2.user.google;

import com.shineidle.tripf.oauth2.user.OAuth2UserUnlink;
import com.shineidle.tripf.oauth2.user.RestTemplateFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class GoogleOAuth2UserUnlink implements OAuth2UserUnlink {
    private static final String URL = "https://oauth2.googleapis.com/revoke";
    private final RestTemplateFactory restTemplateFactory;

    @Override
    public void unlink(String accessToken) {
        RestTemplate restTemplate = restTemplateFactory.create();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("token", accessToken);
        restTemplate.postForObject(URL, params, String.class);
    }
}
