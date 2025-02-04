package com.shineidle.tripf.cart.controller;

import com.shineidle.tripf.cart.dto.CartRequestDto;
import com.shineidle.tripf.cart.dto.CartResponseDto;
import com.shineidle.tripf.cart.service.CartService;
import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.common.message.type.PostMessage;
import io.swagger.v3.oas.annotations.Operation;
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
     * @param productId      상품 식별자
     * @param cartRequestDto {@link CartRequestDto} 장바구니 요청 Dto
     * @return {@link CartResponseDto} 장바구니 응답 Dto
     */
    @Operation(summary = "장바구니 상품 생성")
    @PostMapping("/products/{productId}")
    public ResponseEntity<CartResponseDto> createCart(
            @PathVariable Long productId,
            @Valid @RequestBody CartRequestDto cartRequestDto
    ) {
        CartResponseDto responseDto = cartService.createCart(productId, cartRequestDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * 장바구니 조회
     *
     * @return {@link CartResponseDto} 장바구니 응답 Dto
     */
    @Operation(summary = "장바구니 조회")
    @GetMapping
    public ResponseEntity<List<CartResponseDto>> findCart() {
        List<CartResponseDto> responseDtos = cartService.findCart();

        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }

    /**
     * 장바구니 상품 수량 수정
     *
     * @param productId      상품 식별자
     * @param cartRequestDto {@link CartRequestDto} 장바구니 요청 Dto
     * @return {@link CartResponseDto} 장바구니 응답 Dto
     */
    @Operation(summary = "장바구니 상품 수량 수정")
    @PatchMapping("/products/{productId}")
    public ResponseEntity<CartResponseDto> updateCart(
            @PathVariable Long productId,
            @Valid @RequestBody CartRequestDto cartRequestDto
    ) {
        CartResponseDto responseDto = cartService.updateCart(productId, cartRequestDto);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * 장바구니 상품 삭제
     *
     * @param productId 상품 식별자
     * @return {@link PostMessageResponseDto} 삭제 완료 문구
     */
    @Operation(summary = "장바구니 상품 삭제")
    @DeleteMapping("/products/{productId}")
    public ResponseEntity<PostMessageResponseDto> deleteCart(
            @PathVariable Long productId
    ) {
        cartService.deleteCart(productId);
        PostMessageResponseDto responseDto = new PostMessageResponseDto(PostMessage.PRODUCT_DELETED);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}