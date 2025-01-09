package com.shineidle.tripf.order.repository;

import com.shineidle.tripf.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
