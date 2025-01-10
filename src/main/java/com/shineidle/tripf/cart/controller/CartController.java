package com.shineidle.tripf.cart.controller;

import com.shineidle.tripf.cart.dto.CartCreateRequestDto;
import com.shineidle.tripf.cart.dto.CartCreateResponseDto;
import com.shineidle.tripf.cart.service.CartService;
import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.common.message.enums.PostMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * 장바구니 상품 생성
     *
     * @param productId
     * @param cartCreateRequestDto {@link CartCreateRequestDto}
     * @return {@link CartCreateResponseDto} 생성된 장바구니 응답값
     */
    @PostMapping("/products/{productId}")
    public ResponseEntity<CartCreateResponseDto> createCart(
            @PathVariable Long productId,
            @Valid @RequestBody CartCreateRequestDto cartCreateRequestDto
    ) {

        CartCreateResponseDto responseDto = cartService.createCart(productId, cartCreateRequestDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * 장바구니 조회
     *
     * @return {@link CartCreateResponseDto} 조회된 장바구니 응답값
     */
    @GetMapping
    public ResponseEntity<List<CartCreateResponseDto>> findCart() {

        List<CartCreateResponseDto> responseDtos = cartService.findCart();

        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    /**
     * 장바구니 상품 수량 수정
     *
     * @param productId
     * @param cartCreateRequestDto {@link CartCreateRequestDto}
     * @return {@link CartCreateResponseDto} 수정된 장바구니 응답값
     */
    @PatchMapping("/products/{productId}")
    public ResponseEntity<CartCreateResponseDto> updateCart(
            @PathVariable Long productId,
            @Valid @RequestBody CartCreateRequestDto cartCreateRequestDto
    ) {

        CartCreateResponseDto responseDto = cartService.updateCart(productId, cartCreateRequestDto);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * 장바구니 상품 삭제
     *
     * @param productId
     * @return {@link PostMessageResponseDto} 삭제 완료 메세지 응답값
     */
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<PostMessageResponseDto> deleteCart(
            @PathVariable Long productId
    ) {

        cartService.deleteCart(productId);
        PostMessageResponseDto responseDto = new PostMessageResponseDto(PostMessage.PRODUCT_DELETED);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
