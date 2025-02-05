package com.shineidle.tripf.domain.payment.entity;

import com.shineidle.tripf.domain.order.entity.Order;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "`payments`")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, unique = true)
    private String orderNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = true)
    private Order order;

    @Column(nullable = false, unique = true)
    private String paymentKey;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private String orderName;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String method;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime approvedAt;

    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    protected Payment() {}

    public Payment(String orderNumber, String paymentKey, int amount, String orderName,
                   String status, String method, LocalDateTime createdAt) {
        this.orderNumber = orderNumber;
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