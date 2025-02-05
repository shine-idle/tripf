package com.shineidle.tripf.domain.product.dto;

import com.shineidle.tripf.domain.product.type.ProductCategory;
import com.shineidle.tripf.domain.product.type.ProductStatus;
import com.shineidle.tripf.domain.product.entity.Product;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProductResponseDto {
    private final Long id;
    private final ProductCategory category;
    private final ProductStatus status;
    private final String name;
    private final Long price;
    private final String description;
    private final Long stock;

    public static ProductResponseDto toDto(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getCategory(),
                product.getStatus(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getStock()
        );
    }
}
