package com.shineidle.tripf.product.controller;

import com.shineidle.tripf.product.dto.ProductRequestDto;
import com.shineidle.tripf.product.dto.ProductResponseDto;
import com.shineidle.tripf.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDto> create(
            @RequestBody ProductRequestDto dto
    ) {
        productService.create(dto);
        return null;
    }
}
