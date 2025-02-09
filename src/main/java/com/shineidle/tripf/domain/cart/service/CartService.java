package com.shineidle.tripf.domain.cart.service;

import com.shineidle.tripf.domain.cart.dto.CartRequestDto;
import com.shineidle.tripf.domain.cart.dto.CartResponseDto;

import java.util.List;

public interface CartService {
    /**
     * 장바구니 생성
     *
     * @param productId      상품 식별자
     * @param cartRequestDto {@link CartRequestDto} 장바구니 요청 Dto
     * @return {@link CartResponseDto} 장바구니 응답 Dto
     */
    CartResponseDto createCart(Long productId, CartRequestDto cartRequestDto);

    /**
     * 장바구니 목록 조회
     *
     * @return {@link CartResponseDto} 장바구니 응답 Dto
     */
    List<CartResponseDto> findCart();

    /**
     * 장바구니에 담긴 상품 수량 수정
     *
     * @param productId      상품 식별자
     * @param cartRequestDto {@link CartRequestDto} 장바구니 요청 Dto
     * @return {@link CartResponseDto} 장바구니 응답 Dto
     */
    CartResponseDto updateCart(Long productId, CartRequestDto cartRequestDto);

    /**
     * 장바구니 상품 삭제
     *
     * @param productId 상품 식별자
     */
    void deleteCart(Long productId);
}