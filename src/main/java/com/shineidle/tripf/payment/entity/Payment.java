package com.shineidle.tripf.payment.entity;

import com.shineidle.tripf.common.entity.BaseEntity;
import com.shineidle.tripf.order.entity.Order;
import com.shineidle.tripf.payment.dto.PaymentRequestDto;
import com.shineidle.tripf.payment.type.CancelStatus;
import com.shineidle.tripf.payment.type.PaymentStatus;
import com.shineidle.tripf.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "`payment`")
public class Payment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String paymentKey;  // 토스페이 결제 키
    private String paymentUrl;  // 토스페이 결제 URL

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private CancelStatus cancelStatus  = CancelStatus.PENDING;

    private LocalDateTime createAt;
    private LocalDateTime approvedAt;
    private LocalDateTime canceledAt;

    public Payment() {
    }

    public Long getAmount() {
        return this.order.getTotalPrice();
    }

    // 결제 상태 변경 메서드 추가
    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    //결제 승인처리
    public void approvedPayment(LocalDateTime approvedAt) {
        if (this.cancelStatus != CancelStatus.CANCELLED) {
            this.approvedAt = approvedAt;
        }
    }

    //결제 취소처리
    public void canceledPayment(LocalDateTime canceledAt) {
        if (this.cancelStatus != CancelStatus.CANCELLED) {
            this.cancelStatus = CancelStatus.CANCELLED;
            this.canceledAt = canceledAt;
        }
    }

    public Payment(PaymentRequestDto paymentRequestDto, Order order, User user) {
        if (paymentRequestDto != null) {
            this.paymentKey = paymentRequestDto.getPaymentKey();
            this.paymentStatus = paymentRequestDto.getPaymentStatus() != null ? paymentRequestDto.getPaymentStatus() : PaymentStatus.PENDING;
        } else {
            this.paymentKey = null;
            this.paymentStatus = PaymentStatus.PENDING;
        }
        this.paymentUrl = null;
        this.order = order;
        this.user = user;
        this.cancelStatus = CancelStatus.PENDING;
        this.createAt = LocalDateTime.now();
    }
}