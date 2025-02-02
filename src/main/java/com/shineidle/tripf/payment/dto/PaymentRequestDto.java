package com.shineidle.tripf.payment.dto;

import com.shineidle.tripf.payment.type.PaymentStatus;
import lombok.Getter;

@Getter
public class PaymentRequestDto {
    private final Long orderId;
    private final Long amount;
    private final String paymentKey;
    private final PaymentStatus paymentStatus;  // 결제 상태 (선택적)

    public PaymentRequestDto(Long orderId, Long amount, String paymentKey, PaymentStatus paymentStatus) {
        this.orderId = orderId;
        this.amount = amount;
        this.paymentKey = paymentKey;
        this.paymentStatus = paymentStatus != null ? paymentStatus : PaymentStatus.PENDING;
    }
}