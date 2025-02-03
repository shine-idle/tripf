package com.shineidle.tripf.paymentTest.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "paymentsTest")
public class PaymentTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderId; // 주문 ID (Toss에서 사용)

    @Column(nullable = false, unique = true)
    private String paymentKey; // Toss 결제 키

    @Column(nullable = false)
    private int amount; // 결제 금액

    @Column(nullable = false)
    private String orderName; // 주문명

    @Column(nullable = false)
    private String status; // 결제 상태 (SUCCESS, FAIL, PENDING 등)

    @Column(nullable = false)
    private String method; // 결제 수단 (카드, 계좌이체 등)

    @Column(nullable = false)
    private LocalDateTime createdAt; // 결제 요청 시간

    private LocalDateTime approvedAt; // 결제 승인 시간

    protected PaymentTest() {}

    public PaymentTest(String orderId, String paymentKey, int amount, String orderName,
                   String status, String method, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.paymentKey = paymentKey;
        this.amount = amount;
        this.orderName = orderName;
        this.status = status;
        this.method = method;
        this.createdAt = createdAt;
    }

    public void approve(LocalDateTime approvedAt) {
        this.status = "SUCCESS";
        this.approvedAt = approvedAt;
    }

    public void fail() {
        this.status = "FAIL";
    }
}
