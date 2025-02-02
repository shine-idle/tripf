package com.shineidle.tripf.order.repository;

import com.shineidle.tripf.order.entity.Order;
import com.shineidle.tripf.order.type.OrderStatus;
import com.shineidle.tripf.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

    Optional<Order> findByIdAndUserId(Long id, Long userId);

}
