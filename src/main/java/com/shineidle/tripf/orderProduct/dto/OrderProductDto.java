package com.shineidle.tripf.orderProduct.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderProductDto {
    private final String productName;

    private final Long quantity;

    private final Long purchasePrice;

    private final Long totalPrice;
}