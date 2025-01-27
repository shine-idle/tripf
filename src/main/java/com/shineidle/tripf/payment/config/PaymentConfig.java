package com.shineidle.tripf.payment.cofig;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfig {
    @Value("${payment.toss.test_client_api_key}")
    private String testClientApiKey;

    @Value("${payment.toss.test_secrete_api_key}")
    private String testSecretKey;

    @Value("${payment.toss.success_url}")
    private String successUrl;

    @Value("${payment.toss.fail_url}")
    private String failUrl;

    //토스 페이먼츠에 결제 승인 요청 보낼 URL
    public static final String Url = "https://api.tosspayments.com/v1/payments/";

//    // 결제 승인 요청 URL
//    public static final String CONFIRM_PAYMENT_URL = PAYMENTS_URL + "confirm";
//
//    // 결제 취소 요청 URL
//    public static final String CANCEL_PAYMENT_URL = PAYMENTS_URL + "{paymentKey}/cancel";
}
