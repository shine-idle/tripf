package com.shineidle.tripf.payment.service;

import com.shineidle.tripf.common.util.UserAuthorizationUtil;
import com.shineidle.tripf.order.entity.Order;
import com.shineidle.tripf.order.repository.OrderRepository;
import com.shineidle.tripf.payment.config.PaymentConfig;
import com.shineidle.tripf.payment.dto.*;
import com.shineidle.tripf.payment.entity.Payment;
import com.shineidle.tripf.payment.repository.PaymentRepository;
import com.shineidle.tripf.user.entity.User;
import com.shineidle.tripf.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final RestTemplate restTemplate;
    private final PaymentConfig paymentConfig;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;



    private ResponseEntity<ApiResponse> makeApiRequest(String url, HttpMethod method, HttpEntity<?> entity) {
        return restTemplate.exchange(url, method, entity, ApiResponse.class);
    }

    private HttpHeaders createHeaders(String apiKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    /**
     * 결제 요청
     */
    @Override
    public PaymentDto requestPayment(PaymentRequestDto paymentRequestDto) {
        String url = PaymentConfig.PAYMENT_URL + "request";
        HttpHeaders headers = createHeaders(paymentConfig.getTestClientApiKey());
        HttpEntity<PaymentRequestDto> entity = new HttpEntity<>(paymentRequestDto, headers);

        ResponseEntity<ApiResponse> response = makeApiRequest(url, HttpMethod.POST, entity);

        if (response.getStatusCode().is2xxSuccessful()) {
            Order order = orderRepository.findById(paymentRequestDto.getOrderId())
                    .orElseThrow(() -> new IllegalArgumentException("주문 정보가 없습니다"));


            User user = UserAuthorizationUtil.getLoginUser();

            Payment payment = new Payment(paymentRequestDto, order, user);
            paymentRepository.save(payment);

            return new PaymentDto(payment);
        } else {
            throw new RuntimeException("결제 요청에 실패했습니다");
        }
    }

    /**
     * 결제 승인
     */
    @Override
    public PaymentDto confirmPayment(PaymentConfirmDto paymentConfirmDto) {
        Payment payment = paymentRepository.findByPaymentKey(paymentConfirmDto.getPaymentKey())
                .orElseThrow(() -> new IllegalArgumentException("결제 정보가 없습니다"));

        String url = PaymentConfig.PAYMENT_URL + "confirm";

        String email = UserAuthorizationUtil.getLoginUserEmail();

        if (!payment.getUser().getEmail().equals(email)) {
            throw new RuntimeException("인증된 사용자만 결제를 승인할 수 있습니다.");
        }

        HttpHeaders headers = createHeaders(paymentConfig.getTestSecretApiKey());

        HttpEntity<PaymentConfirmDto> entity = new HttpEntity<>(paymentConfirmDto, headers);

        ResponseEntity<ApiResponse> response = makeApiRequest(url, HttpMethod.POST, entity);

        if (response.getStatusCode().is2xxSuccessful()) {
            payment.approvedPayment(LocalDateTime.now());
            paymentRepository.save(payment);
        } else {
            throw new RuntimeException("결제에 실패했습니다");
        }

        return new PaymentDto(payment);
    }

    /**
     * 결제 취소
     */
    @Override
    public PaymentDto cancelPayment(PaymentCancelDto paymentCancelDto) {
        Payment payment = paymentRepository.findByPaymentKey(paymentCancelDto.getPaymentKey())
                .orElseThrow(() -> new IllegalArgumentException("결제 정보가 없습니다"));

        String url = PaymentConfig.PAYMENT_URL + "cancel";

        User user = UserAuthorizationUtil.getLoginUser();
        if (!payment.getUser().getEmail().equals(user.getEmail())) {
            throw new RuntimeException("인증된 사용자만 결제 취소를 할 수 있습니다.");
        }

        HttpHeaders headers = createHeaders(paymentConfig.getTestSecretApiKey());
        HttpEntity<PaymentCancelDto> entity = new HttpEntity<>(paymentCancelDto, headers);

        ResponseEntity<ApiResponse> response = makeApiRequest(url, HttpMethod.POST, entity);

        if (response.getStatusCode().is2xxSuccessful()) {
            payment.canceledPayment(LocalDateTime.now());
            paymentRepository.save(payment);
        } else {
            throw new RuntimeException("결제 취소에 실패했습니다");
        }

        return new PaymentDto(payment);
    }

    /**
     * 결제 정보를 조회
     */
    @Override
    public PaymentDto getPaymentDetails(String paymentKey) {
        Payment payment = paymentRepository.findByPaymentKey(paymentKey)
                .orElseThrow(() -> new IllegalArgumentException("결제 정보가 없습니다"));

        return new PaymentDto(payment);
    }

    /**
     * 결제 취소 정보를 조회
     */
    @Override
    public PaymentDto getCancelDetails(String paymentKey) {
        Payment payment = paymentRepository.findByPaymentKey(paymentKey)
                .orElseThrow(() -> new IllegalArgumentException("취소 정보가 없습니다"));

        return new PaymentDto(payment);
    }
}

/*
    // 결제 요청을 처리하는 메서드
    @Override
    public PaymentDto requestPayment(PaymentRequestDto paymentRequestDto) {
        String url = PaymentConfig.PAYMENT_URL + "request";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + paymentConfig.getTestClientApiKey());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PaymentRequestDto> entity = new HttpEntity<>(paymentRequestDto, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {

            Order order = orderRepository.findById(Long.parseLong(paymentRequestDto.getOrderId()))
                    .orElseThrow(() -> new IllegalArgumentException("주문 정보가 없습니다"));

            String email = this.getAuthenticatedEmail();

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("사용자 정보가 없습니다"));

            Payment payment = new Payment(paymentRequestDto, order, user);
            paymentRepository.save(payment);

            return new PaymentDto(payment);
        } else {
            throw new RuntimeException("결제 요청에 실패했습니다");
        }
    }


    @Override
    public PaymentDto confirmPayment(PaymentConfirmDto paymentConfirmDto) {
        Payment payment = paymentRepository.findByPaymentKey(paymentConfirmDto.getPaymentKey())
                .orElseThrow(() -> new IllegalArgumentException("결제 정보가 없습니다"));

        String url = PaymentConfig.PAYMENT_URL + "confirm";

        String email = this.getAuthenticatedEmail();

        if (!payment.getUser().getEmail().equals(email)) {
            throw new RuntimeException("인증된 사용자만 결제를 승인할 수 있습니다.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + paymentConfig.getTestSecretApiKey());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PaymentConfirmDto> entity = new HttpEntity<>(paymentConfirmDto, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            payment.approvedPayment(LocalDateTime.now());
            paymentRepository.save(payment);
        } else {
            throw new RuntimeException("결제에 실패했습니다");
        }

        return new PaymentDto(payment);
    }

    @Override
    public PaymentDto cancelPayment(PaymentCancelDto paymentCancelDto) {
        Payment payment = paymentRepository.findByPaymentKey(paymentCancelDto.getPaymentKey())
                .orElseThrow(() -> new IllegalArgumentException("결제 정보가 없습니다"));

        String url = PaymentConfig.PAYMENT_URL + "cancel";

        String email = this.getAuthenticatedEmail();

        if (!payment.getUser().getEmail().equals(email)) {
            throw new RuntimeException("인증된 사용자만 결제 취소를 할 수 있습니다.");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + paymentConfig.getTestSecretApiKey());
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PaymentCancelDto> entity = new HttpEntity<>(paymentCancelDto, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            payment.canceledPayment(LocalDateTime.now());
            paymentRepository.save(payment);
        } else {
            throw new RuntimeException("결제 취소에 실패했습니다");
        }

        return new PaymentDto(payment);
    }

    private String getAuthenticatedEmail() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public PaymentDto getCancelDetails(String paymentKey) {
        Payment payment = paymentRepository.findByPaymentKey(paymentKey)
                .orElseThrow(() -> new IllegalArgumentException("취소 정보가 없습니다"));

        return new PaymentDto(payment);
    }

    @Override
    public PaymentDto getPaymentDetails(String paymentKey) {
        Payment payment = paymentRepository.findByPaymentKey(paymentKey)
                .orElseThrow(() -> new IllegalArgumentException("결제 정보가 없습니다"));

        return new PaymentDto(payment);
    }
}
 */