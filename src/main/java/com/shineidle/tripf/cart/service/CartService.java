package com.shineidle.tripf.cart.service;

import com.shineidle.tripf.cart.dto.CartRequestDto;
import com.shineidle.tripf.cart.dto.CartResponseDto;

import java.util.List;

public interface CartService {
    /**
     *장바구니 상품 추가
     */
    CartResponseDto createCart(Long productId, CartRequestDto cartRequestDto);

    /**
     * 장바구니 목록 조회
     */
    List<CartResponseDto> findCart();

    /**
     * 장바구니에 담긴 상품 수량 수정
     */
    CartResponseDto updateCart(Long productId, CartRequestDto cartRequestDto);

    /**
     * 장바구니 상품 삭제
     */
    void deleteCart(Long productId);
}