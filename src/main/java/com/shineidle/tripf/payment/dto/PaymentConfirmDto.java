package com.shineidle.tripf.payment.dto;

import lombok.Getter;

@Getter
public class PaymentConfirmDto {
    private final String paymentKey;
    private final String orderId;
    private final Long amount;

    public PaymentConfirmDto(String paymentKey, String orderId, Long amount) {
        this.paymentKey = paymentKey;
        this.orderId = orderId;
        this.amount = amount;
    }
}