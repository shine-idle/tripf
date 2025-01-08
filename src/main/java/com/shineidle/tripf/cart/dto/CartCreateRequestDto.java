package com.shineidle.tripf.cart.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CartCreateRequestDto {

    @NotBlank(message = "상품 수량을 입력해주세요.")
    private final Long quantity;
}
