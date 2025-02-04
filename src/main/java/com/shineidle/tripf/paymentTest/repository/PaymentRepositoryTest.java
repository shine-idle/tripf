package com.shineidle.tripf.paymentTest.repository;

import com.shineidle.tripf.paymentTest.entity.PaymentTest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepositoryTest extends JpaRepository<PaymentTest, Long> {
    PaymentTest findByOrderId(String orderId);
}
