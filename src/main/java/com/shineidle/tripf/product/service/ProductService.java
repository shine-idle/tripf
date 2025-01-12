package com.shineidle.tripf.product.service;

import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.product.dto.ProductRequestDto;
import com.shineidle.tripf.product.dto.ProductResponseDto;

import java.util.List;

public interface ProductService {
    ProductResponseDto create(ProductRequestDto dto);

    ProductResponseDto update(Long productId, ProductRequestDto dto);

    PostMessageResponseDto delete(Long productId);

    List<ProductResponseDto> find();

    ProductResponseDto find(Long productId);
}
