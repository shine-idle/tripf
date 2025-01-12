package com.shineidle.tripf.product.controller;

import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.product.dto.ProductRequestDto;
import com.shineidle.tripf.product.dto.ProductResponseDto;
import com.shineidle.tripf.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/products")
public class AdminProductController {
    private final ProductService productService;

    /**
     * 상품 생성
     * @param dto {@link ProductRequestDto}
     * @return {@link ProductResponseDto}
     */
    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(
            @RequestBody @Valid ProductRequestDto dto
    ) {
        return new ResponseEntity<>(productService.createProduct(dto), HttpStatus.CREATED);
    }

    /**
     * 상품 수정
     * @param productId 상품Id
     * @param dto {@link ProductRequestDto}
     * @return {@link ProductResponseDto}
     */
    @PatchMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> updateProduct(
            @PathVariable Long productId,
            @RequestBody @Valid ProductRequestDto dto
    ) {
        return new ResponseEntity<>(productService.updateProduct(productId, dto), HttpStatus.OK);
    }

    /**
     * 상품 삭제
     * @param productId 상품Id
     * @return 상품 삭제 문구
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<PostMessageResponseDto> deleteProduct(
            @PathVariable Long productId
    ) {
        return new ResponseEntity<>(productService.deleteProduct(productId), HttpStatus.OK);
    }
}
