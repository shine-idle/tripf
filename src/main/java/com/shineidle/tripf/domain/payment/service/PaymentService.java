package com.shineidle.tripf.domain.payment.service;

import java.util.Map;

public interface PaymentService {

    Map<String, String> createPaymentRequest(int amount);

    void confirmPayment(String paymentKey, String orderId, int amount);
}
