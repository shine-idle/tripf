package com.shineidle.tripf.cart.service;

import com.shineidle.tripf.cart.dto.CartCreateRequestDto;
import com.shineidle.tripf.cart.dto.CartCreateResponseDto;

import java.util.List;

public interface CartService {

    CartCreateResponseDto createCart(Long productId, CartCreateRequestDto cartCreateRequestDto);

    List<CartCreateResponseDto> findCart();

    CartCreateResponseDto updateCart(Long productId, CartCreateRequestDto cartCreateRequestDto);

    void deleteCart(Long productId);
}
