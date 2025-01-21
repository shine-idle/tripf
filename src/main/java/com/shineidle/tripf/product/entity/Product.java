package com.shineidle.tripf.product.entity;

import com.shineidle.tripf.common.BaseEntity;
import com.shineidle.tripf.photo.entity.ProductPhoto;
import com.shineidle.tripf.product.type.ProductCategory;
import com.shineidle.tripf.product.type.ProductStatus;
import com.shineidle.tripf.product.dto.ProductRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "`product`")
@DynamicUpdate
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Column(length = 50)
    private String name;

    private Long price;

    @Lob
    private String description;

    private Long stock;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductPhoto> productPhotos = new ArrayList<>();

    public Product(ProductCategory category, ProductStatus status, String name, Long price, String description, Long stock) {
        this.category = category;
        this.status = status;
        this.name = name;
        this.price = price;
        this.description = description;
        this.stock = stock;
    }
    protected Product() {}

    public void update(ProductRequestDto dto) {
        this.category = dto.getCategory();
        this.status = dto.getStatus();
        this.name = dto.getName();
        this.price = dto.getPrice();
        this.description = dto.getDescription();
        this.stock = dto.getStock();
    }

    public void delete() {
        this.status = ProductStatus.DISCONTINUED;
    }
}
