package com.shineidle.tripf.order.dto;

import com.shineidle.tripf.order.entity.Order;
import com.shineidle.tripf.order.type.OrderStatus;
import com.shineidle.tripf.order.type.PayStatus;
import com.shineidle.tripf.orderProduct.entity.OrderProduct;
import com.shineidle.tripf.product.entity.Product;
import com.shineidle.tripf.user.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class OrderRequestDto {
    @NotNull(message = "상품 ID는 필수입니다")
    private Long productId;

    @NotNull(message = "수량은 필수입니다")
    private Long quantity;

    public Order toEntity(Product product, User user) {
        long totalPrice = product.getPrice() * this.quantity;

        OrderProduct orderProduct = new OrderProduct(this.quantity, product);

        Order order = new Order(
                totalPrice,
                OrderStatus.ORDER_RECEIVED,
                PayStatus.PENDING,
                product,
                user,
                LocalDateTime.now()
        );

        order.addOrderProduct(orderProduct);
        return order;
    }
}