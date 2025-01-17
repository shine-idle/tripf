package com.shineidle.tripf.cart.service;

import com.shineidle.tripf.cart.dto.CartRequestDto;
import com.shineidle.tripf.cart.dto.CartResponseDto;

import java.util.List;

public interface CartService {

    CartResponseDto createCart(Long productId, CartRequestDto cartRequestDto);

    List<CartResponseDto> findCart();

    CartResponseDto updateCart(Long productId, CartRequestDto cartRequestDto);

    void deleteCart(Long productId);
}
