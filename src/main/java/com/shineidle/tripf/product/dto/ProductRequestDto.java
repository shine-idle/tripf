package com.shineidle.tripf.product.dto;

import com.shineidle.tripf.product.ProductCategory;
import com.shineidle.tripf.product.ProductStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProductRequestDto {
    private final ProductCategory category;

    @NotBlank(message = "상품 이름은 비워둘 수 없습니다.")
    @Size(min = 1, max = 50, message = "상품명은 최소 1자에서 50자까지 입력하실 수 있습니다.")
    private final String name;

    @NotNull(message = "가격은 비워둘 수 없습니다.")
    @Min(value = 1, message = "값은 0보다 높아야합니다.")
    private final Long price;

    private final String description;

    @Min(value = 0, message = "재고는 0보다 낮을 수 없습니다.")
    private final Long stock;

    private final ProductStatus status;
}
