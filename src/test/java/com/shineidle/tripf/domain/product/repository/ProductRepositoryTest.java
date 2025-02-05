package com.shineidle.tripf.domain.product.repository;

import com.shineidle.tripf.domain.product.entity.Product;
import com.shineidle.tripf.domain.product.type.ProductCategory;
import com.shineidle.tripf.domain.product.type.ProductStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    private Product activeProduct;
    private Product discontinuedProduct;

    @BeforeEach
    void setUp() {
        activeProduct = new Product(
                ProductCategory.GOODS,
                ProductStatus.IN_STOCK,
                "activeProduct",
                123123L,
                "this is activeProduct",
                100L
        );

        discontinuedProduct = new Product(
                ProductCategory.GOODS,
                ProductStatus.DISCONTINUED,
                "discontinuedProduct",
                123123L,
                "this is discontinuedProduct",
                100L
        );
    }

    @Test
    @DisplayName("단종되지 않은 상품 조회")
    void findAllExceptDiscontinuedProducts() {
        // Given
        productRepository.save(activeProduct);
        productRepository.save(discontinuedProduct);

        // When
        List<Product> products = productRepository.findAllExceptDiscontinuedProducts();

        // Then
        assertThat(products).containsExactly(activeProduct);
        assertThat(products).doesNotContain(discontinuedProduct);
    }
}