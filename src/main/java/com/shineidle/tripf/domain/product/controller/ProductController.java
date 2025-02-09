package com.shineidle.tripf.domain.product.controller;

import com.shineidle.tripf.domain.product.dto.ProductResponseDto;
import com.shineidle.tripf.domain.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    /**
     * 상품 조회(다건)
     *
     * @return {@link ProductResponseDto} 상품 응답 Dto
     */
    @Operation(summary = "상품 조회(다건)")
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> findAllProduct(
    ) {
        return new ResponseEntity<>(productService.findAllProduct(), HttpStatus.OK);
    }

    /**
     * 상품 조회(단건)
     *
     * @param productId 상품 식별자
     * @return {@link ProductResponseDto} 상품 응답 Dto
     */
    @Operation(summary = "상품 조회(단건)")
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> findProduct(
            @PathVariable Long productId
    ) {
        return new ResponseEntity<>(productService.findProduct(productId), HttpStatus.OK);
    }
}
