package com.shineidle.tripf.cart.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CartRequestDto {

    @NotNull(message = "상품 수량을 입력해주세요.")
    private final Long quantity;
}