package com.shineidle.tripf.domain.payment.service;

import com.shineidle.tripf.domain.payment.entity.Payment;
import com.shineidle.tripf.domain.payment.repository.PaymentRepository;
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
public class PaymentService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final PaymentRepository paymentRepository;

    @Value("${toss.secret-key}")
    private String secretKey;

    /**
     * 결제 요청 생성
     *
     * @param amount 결제 금액
     * @return 결제 요청 정보를 가진 {@link Map}
     */
    public Map<String, String> createPaymentRequest(int amount) {
        String orderId = "ORDER-" + System.currentTimeMillis();

        Map<String, String> response = new HashMap<>();
        response.put("orderId", orderId);
        response.put("amount", String.valueOf(amount));

        return response;
    }

    /**
     * 결제 승인 및 정보 저장
     *
     * @param paymentKey 결제키
     * @param orderId 주문ID
     * @param amount 결제 금액
     */
    @Transactional
    public void confirmPayment(String paymentKey, String orderId, int amount) {
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
            Payment payment = new Payment(
                    orderId, paymentKey, amount, "테스트 결제",
                    "SUCCESS", "카드", LocalDateTime.now()
            );
            paymentRepository.save(payment);
        }
    }
}