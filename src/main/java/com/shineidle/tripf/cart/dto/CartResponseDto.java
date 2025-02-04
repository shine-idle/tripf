package com.shineidle.tripf.cart.dto;

import com.shineidle.tripf.cart.entity.Cart;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CartResponseDto {
    private final Long cartId;
    private final Long userId;
    private final Long productId;
    private final Long quantity;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public static CartResponseDto toDto(Cart cart) {
        return new CartResponseDto(
                cart.getId(),
                cart.getUser().getId(),
                cart.getProduct().getId(),
                cart.getQuantity(),
                cart.getCreatedAt(),
                cart.getModifiedAt()
        );
    }
}
