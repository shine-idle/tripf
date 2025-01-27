package com.shineidle.tripf.payment.dto;

import lombok.Getter;

@Getter
public class PaymentCancelDto {
    private final String paymentKey;
    private final String cancelReason;
    private final Long cancelAmount;

    public PaymentCancelDto(String paymentKey, String cancelReason, Long cancelAmount) {
        this.paymentKey = paymentKey;
        this.cancelReason = cancelReason;
        this.cancelAmount = cancelAmount;
    }
}
