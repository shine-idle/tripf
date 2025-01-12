package com.shineidle.tripf.product.controller;

import com.shineidle.tripf.product.dto.ProductResponseDto;
import com.shineidle.tripf.product.service.ProductService;
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
     * @return {@link ProductResponseDto}
     */
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> find() {
        return new ResponseEntity<>(productService.find(), HttpStatus.OK);
    }

    /**
     * 상품 조회(단건)
     * @param productId 상품Id
     * @return {@link ProductResponseDto}
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> find(@PathVariable Long productId) {
        return new ResponseEntity<>(productService.find(productId), HttpStatus.OK);
    }
}
