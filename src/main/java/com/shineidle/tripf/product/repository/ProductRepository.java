package com.shineidle.tripf.product.repository;

import com.shineidle.tripf.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    /**
     * 단종된 상품을 제외한 모든 상품 리턴
     * @return 상품 리스트
     */
    @Query("select pr from Product pr where pr.status != 'DISCONTINUED'")
    List<Product> findAllExceptDiscontinuedProducts();
}
