//package com.shineidle.tripf.payment.repository;
//
//import com.shineidle.tripf.payment.entity.Payment;
//import nonapi.io.github.classgraph.fileslice.Slice;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.Optional;
//
//public interface JpaPaymentRepository extends JpaRepository<Payment, Long> {
//    Optional<Payment> findByOrderId(String orderId);
//
//    Optional<Payment> findByPaymentKeyAndCustomer_Email(String paymentKey, String email);
//
//    Slice<payment> findAllByCustomer_Email(String email, Pageable pageable);
//}
