package com.shineidle.tripf.oauth2.user;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

// TODO : javadoc 작성
@Component
public class RestTemplateFactory {
    private final RestTemplateBuilder restTemplateBuilder;

    public RestTemplateFactory(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }

    //RestTemplate / OAuth2 연결 끊기 기능 구현시 HTTP API 요청을 위해 사용
    public RestTemplate create() {
        return restTemplateBuilder.build();
    }
}
