package com.shineidle.tripf.domain.payment.dto;

public class PaymentConfirmRequestDto {
    private String paymentKey;
    private String orderId;
    private int amount;

    public String getPaymentKey() {
        return paymentKey;
    }

    public String getOrderId() {
        return orderId;
    }

    public int getAmount() {
        return amount;
    }
}
