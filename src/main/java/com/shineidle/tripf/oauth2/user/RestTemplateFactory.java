package com.shineidle.tripf.oauth2.user;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateFactory {
    private final RestTemplateBuilder restTemplateBuilder;

    /**
     * RestTemplateFactory 생성자.
     *
     * @param restTemplateBuilder RestTemplate 생성을 위한 빌더 객체
     */
    public RestTemplateFactory(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }

    /**
     * 새로운 RestTemplate 객체를 생성하여 반환합니다.
     *
     * @return 생성된 RestTemplate 객체
     */
    public RestTemplate create() {
        return restTemplateBuilder.build();
    }
}
