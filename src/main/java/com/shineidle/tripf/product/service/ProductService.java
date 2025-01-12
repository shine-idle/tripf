package com.shineidle.tripf.product.service;

import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.product.dto.ProductRequestDto;
import com.shineidle.tripf.product.dto.ProductResponseDto;

import java.util.List;

public interface ProductService {
    ProductResponseDto createProduct(ProductRequestDto dto);

    ProductResponseDto updateProduct(Long productId, ProductRequestDto dto);

    PostMessageResponseDto deleteProduct(Long productId);

    List<ProductResponseDto> findAllProduct();

    ProductResponseDto findProduct(Long productId);
}
