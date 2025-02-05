package com.shineidle.tripf.domain.order.entity;

import com.shineidle.tripf.global.common.entity.BaseEntity;
import com.shineidle.tripf.domain.order.type.OrderStatus;
import com.shineidle.tripf.domain.payment.type.PayStatus;
import com.shineidle.tripf.domain.orderProduct.entity.OrderProduct;
import com.shineidle.tripf.domain.product.entity.Product;
import com.shineidle.tripf.domain.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "`order`")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull(message = "총 가격을 입력해주세요")
    private Long totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus = OrderStatus.ORDER_RECEIVED;

    @Enumerated(EnumType.STRING)
    private PayStatus payStatus = PayStatus.PENDING;

    @ManyToOne
    private Product product;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts = new ArrayList<>();

    private LocalDateTime createdAt;

    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    public Order() {
    }

    public Order(Long totalPrice, OrderStatus orderStatus, PayStatus payStatus, Product product, User user, LocalDateTime createdAt) {
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.payStatus = payStatus;
        this.product = product;
        this.user = user;
        this.createdAt = createdAt;
    }

    /**
     * 연관관계 편의 메소드 - 반대쪽에는 연관관계 편의 메소드가 없도록 주의합니다.
     */
    public void addOrderProduct(OrderProduct orderProduct) {
        this.orderProducts.add(orderProduct);
        orderProduct.updateOrder(this);
        updateTotalPrice();
    }

    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     */
    private void updateTotalPrice() {
        this.totalPrice = this.orderProducts.stream()
                .mapToLong(OrderProduct::getTotalPrice)
                .sum();
    }
}