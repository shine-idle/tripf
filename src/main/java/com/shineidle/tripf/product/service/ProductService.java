package com.shineidle.tripf.product.service;

import com.shineidle.tripf.product.dto.ProductRequestDto;
import com.shineidle.tripf.product.dto.ProductResponseDto;

public interface ProductService {
    ProductResponseDto create(ProductRequestDto dto);
}
