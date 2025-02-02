package com.shineidle.tripf.payment.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class PaymentConfig {
    @Value("${TOSS_CLIENT_KEY}")
    private String testClientApiKey;

    @Value("${TOSS_SECRET_KEY}")
    private String testSecretKey;

    public static final String PAYMENT_URL = "https://api.tosspayments.com/v1/payments/";

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public String getTestSecretApiKey() {
        return testSecretKey;
    }

    public String getTestClientApiKey() {
        return testClientApiKey;
    }
}