package com.shineidle.tripf.domain.product.dto;

import com.shineidle.tripf.domain.photo.entity.ProductPhoto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProductPhotoDto {
    private final Long id;
    private final String imageUrl;

    public static ProductPhotoDto toDto(ProductPhoto productPhoto) {
        return new ProductPhotoDto(productPhoto.getPhoto().getId(), productPhoto.getPhoto().getUrl());
    }
}
