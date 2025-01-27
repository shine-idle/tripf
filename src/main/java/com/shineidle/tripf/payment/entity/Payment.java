package com.shineidle.tripf.payment.entity;

import com.shineidle.tripf.common.BaseEntity;
import com.shineidle.tripf.order.entity.Order;
import com.shineidle.tripf.payment.type.CancelStatus;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //결제 상태도 여기로 옮겨야 함

    @Enumerated(EnumType.STRING)
    private CancelStatus cancelStatus;

    private LocalDateTime createAt;
    private LocalDateTime approvedAt;
    private LocalDateTime canceledAt;

    public Long getAmount() {
        return this.order.getTotalPrice();
    }

    public void approvedPayment(LocalDateTime approvedAt) {
        if(this.cancelStatus != CancelStatus.CANCELLED) {
            this.approvedAt = approvedAt;
        }
    }

    public void canceledPayment(LocalDateTime canceledAt) {
        if(this.cancelStatus != CancelStatus.CANCELLED) {
            this.cancelStatus = CancelStatus.CANCELLED;
            this.canceledAt = canceledAt;
        }
    }
}