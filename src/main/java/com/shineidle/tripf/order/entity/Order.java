package com.shineidle.tripf.order.entity;

import com.shineidle.tripf.common.BaseEntity;
import com.shineidle.tripf.order.type.OrderStatus;
import com.shineidle.tripf.order.type.PayStatus;
import com.shineidle.tripf.orderProduct.entity.OrderProduct;
import com.shineidle.tripf.product.entity.Product;
import com.shineidle.tripf.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;
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

    @OneToMany(mappedBy = "order")
    private List<OrderProduct> orderProducts;

    private LocalDateTime createdAt;

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

    public void addOrderProduct(OrderProduct orderProduct) {
        orderProduct.setOrder(this);
        this.orderProducts.add(orderProduct);
    }
}