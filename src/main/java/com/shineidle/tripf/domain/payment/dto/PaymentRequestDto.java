package com.shineidle.tripf.domain.payment.dto;

import lombok.Getter;

@Getter
public class PaymentRequestDto {
    private int amount;

    public int getAmount() {
        return amount;
    }
}