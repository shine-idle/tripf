package com.shineidle.tripf.payment.repository;

import com.shineidle.tripf.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaymentKey(String paymentKey);

    Optional<Payment> findByOrderId(Long orderId); // OrderId로 결제 정보 찾기
}