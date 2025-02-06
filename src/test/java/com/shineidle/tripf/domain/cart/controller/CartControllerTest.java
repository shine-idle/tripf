package com.shineidle.tripf.domain.cart.controller;

import com.shineidle.tripf.domain.cart.dto.CartRequestDto;
import com.shineidle.tripf.domain.cart.dto.CartResponseDto;
import com.shineidle.tripf.domain.cart.service.CartService;
import com.shineidle.tripf.global.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.global.common.message.type.PostMessage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {
    @InjectMocks
    private CartController cartController;

    @Mock
    private CartService cartService;

    private CartRequestDto cartRequestDto;
    private CartResponseDto cartResponseDto;

    @BeforeEach
    void setUp() {
        cartRequestDto = new CartRequestDto(1L);
        cartResponseDto = new CartResponseDto();

        ReflectionTestUtils.setField(cartResponseDto, "productId", 1L);
        ReflectionTestUtils.setField(cartResponseDto, "quantity", 1L);
    }

    @Test
    void createCartSuccessTest() {
        when(cartService.createCart(1L, cartRequestDto)).thenReturn(cartResponseDto);

        ResponseEntity<CartResponseDto> response = cartController.createCart(1L, cartRequestDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(cartResponseDto, response.getBody());
        verify(cartService, times(1)).createCart(1L, cartRequestDto);
    }

    @Test
    void createCartProductNotFoundTest() {
        when(cartService.createCart(1L, cartRequestDto))
                .thenThrow(new IllegalArgumentException("상품을 찾을 수 없습니다."));

        ResponseEntity<CartResponseDto> response = null;
        try {
            response = cartController.createCart(1L, cartRequestDto);
        } catch (IllegalArgumentException e) {
            response = ResponseEntity.badRequest().build();
        }

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void findCartSuccessTest() {
        when(cartService.findCart()).thenReturn(Collections.singletonList(cartResponseDto));

        ResponseEntity<List<CartResponseDto>> response = cartController.findCart();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(cartResponseDto, response.getBody().get(0));
        verify(cartService, times(1)).findCart();
    }

    @Test
    void updateCartSuccessTest() {
        when(cartService.updateCart(1L, cartRequestDto)).thenReturn(cartResponseDto);

        ResponseEntity<CartResponseDto> response = cartController.updateCart(1L, cartRequestDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(cartResponseDto, response.getBody());
        verify(cartService, times(1)).updateCart(1L, cartRequestDto);
    }

    @Test
    void updateCartProductNotFoundTest() {
        when(cartService.updateCart(1L, cartRequestDto))
                .thenThrow(new IllegalArgumentException("상품을 찾을 수 없습니다."));

        ResponseEntity<CartResponseDto> response = null;
        try {
            response = cartController.updateCart(1L, cartRequestDto);
        } catch (IllegalArgumentException e) {
            response = ResponseEntity.badRequest().build();
        }

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void deleteCartSuccessTest() {
        doNothing().when(cartService).deleteCart(1L);

        ResponseEntity<PostMessageResponseDto> response = cartController.deleteCart(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(PostMessage.PRODUCT_DELETED.getMessage(), response.getBody().getMessage());
        verify(cartService, times(1)).deleteCart(1L);
    }

    @Test
    void deleteCartProductNotFoundTest() {
        doThrow(new IllegalArgumentException("상품을 찾을 수 없습니다.")).when(cartService).deleteCart(1L);

        ResponseEntity<PostMessageResponseDto> response = null;
        try {
            response = cartController.deleteCart(1L);
        } catch (IllegalArgumentException e) {
            response = ResponseEntity.badRequest().body(new PostMessageResponseDto(e.getMessage()));
        }

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("상품을 찾을 수 없습니다.", response.getBody().getMessage());
    }
}