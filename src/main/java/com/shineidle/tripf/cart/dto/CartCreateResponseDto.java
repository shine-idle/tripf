package com.shineidle.tripf.cart.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CartCreateResponseDto {

    private final Long cartId;
    private final Long userId;
    private final Long productId;
    private final Long quantity;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
}
