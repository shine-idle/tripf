package com.shineidle.tripf.payment.dto;

import com.shineidle.tripf.payment.entity.Payment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class PaymentDto {
    private final Long id;
    private String paymentKey;
    private String paymentUrl;
    private Long amount;
    private Long orderId;
    private Long userId;
    private String cancelStatus;
    private LocalDateTime createAt;
    private LocalDateTime approvedAt;
    private LocalDateTime canceledAt;

    public PaymentDto(Payment payment) {
        this.id = payment.getId();
        this.paymentKey = payment.getPaymentKey();
        this.paymentUrl = payment.getPaymentUrl();
        this.amount = payment.getAmount();
        this.orderId = payment.getOrder().getId();
        this.userId = payment.getUser().getId();
        this.cancelStatus = payment.getCancelStatus() != null ? payment.getCancelStatus().name() : null;
        this.createAt = payment.getCreateAt();
        this.approvedAt = payment.getApprovedAt() != null ? payment.getApprovedAt() : null;
        this.canceledAt = payment.getCanceledAt() != null ? payment.getCanceledAt() : null;
    }

    public PaymentDto(Long id, String paymentKey, Long amount, Long orderId) {
        this.id = id;
        this.paymentKey = paymentKey;
        this.amount = amount;
        this.orderId = orderId;
        this.createAt = LocalDateTime.now();
    }
}