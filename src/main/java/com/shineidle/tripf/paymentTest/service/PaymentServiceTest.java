package com.shineidle.tripf.paymentTest.service;

import com.shineidle.tripf.paymentTest.entity.PaymentTest;
import com.shineidle.tripf.paymentTest.repository.PaymentRepositoryTest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Getter
@RequiredArgsConstructor
public class PaymentServiceTest {

    private final RestTemplate restTemplate = new RestTemplate();
    private final PaymentRepositoryTest paymentRepositoryTest;// Toss 시크릿 키

    @Value("${toss.secret-key}")
    private String secretKey;

    public Map<String, String> createPaymentRequestTest(int amount) {
        String orderId = "ORDER-" + System.currentTimeMillis();

        Map<String, String> response = new HashMap<>();
        response.put("orderId", orderId);
        response.put("amount", String.valueOf(amount));

        return response;
    }

    @Transactional
    public void confirmPaymentTest(String paymentKey, String orderId, int amount) {
        String url = "https://api.tosspayments.com/v1/payments/confirm";

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(secretKey, "");
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("paymentKey", paymentKey);
        body.put("orderId", orderId);
        body.put("amount", String.valueOf(amount));

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            PaymentTest paymentTest = new PaymentTest(
                    orderId, paymentKey, amount, "테스트 결제",
                    "SUCCESS", "카드", LocalDateTime.now()
            );
            paymentRepositoryTest.save(paymentTest);
        }
    }
}