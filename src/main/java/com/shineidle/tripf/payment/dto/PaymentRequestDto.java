package com.shineidle.tripf.payment.dto;

import lombok.Getter;

@Getter
public class PaymentRequestDto {
    private int amount;

    public int getAmount() {
        return amount;
    }
}
