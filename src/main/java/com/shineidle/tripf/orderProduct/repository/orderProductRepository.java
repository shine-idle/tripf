package com.shineidle.tripf.orderProduct.repository;

import com.shineidle.tripf.orderProduct.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface orderProductRepository extends JpaRepository<OrderProduct, Long> {
}
