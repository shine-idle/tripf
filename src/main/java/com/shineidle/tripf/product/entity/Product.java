package com.shineidle.tripf.product.entity;

import com.shineidle.tripf.common.BaseEntity;
import com.shineidle.tripf.product.ProductCategory;
import com.shineidle.tripf.product.ProductStatus;
import jakarta.persistence.*;

@Entity
@Table(name = "product")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Column(length = 50)
    private String name;

    private Long price;

    private String description;

    private Long stock;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    public Product(ProductCategory category, String name, Long price, String description, Long stock, ProductStatus status) {
        this.category = category;
        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
        this.status = status;
    }
    protected Product() {}
}
