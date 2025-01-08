package com.shineidle.tripf.product.repository;

import com.shineidle.tripf.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
