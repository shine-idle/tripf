package com.shineidle.tripf.order.dto;

import com.shineidle.tripf.order.entity.Order;
import com.shineidle.tripf.order.type.OrderStatus;
import com.shineidle.tripf.orderProduct.dto.OrderProductDto;
import com.shineidle.tripf.orderProduct.entity.OrderProduct;
import com.shineidle.tripf.payment.entity.Payment;
import com.shineidle.tripf.payment.type.PaymentStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class OrderResponseDto {
    private final Long id;
    private final String productName;
    private final List<OrderProductDto> orderProducts;
    private final Long totalPrice;
    private final OrderStatus orderStatus;
    private final PaymentStatus paymentStatus;
    private final LocalDateTime createTime;

    public static OrderResponseDto toDto(Order order) {
        Payment payment = order.getPayment(); // 결제 정보
        PaymentStatus paymentStatus = (payment != null) ? payment.getPaymentStatus() : PaymentStatus.PENDING;

        List<OrderProductDto> orderProductDtos = order.getOrderProducts().stream()
                .map(orderProduct -> new OrderProductDto(
                        orderProduct.getProduct().getName(),
                        orderProduct.getQuantity(),
                        orderProduct.getPurchasePrice(),
                        orderProduct.getTotalPrice()
                ))
                .collect(Collectors.toList());

        Long totalPrice = order.getOrderProducts().stream()
                .mapToLong(OrderProduct::getTotalPrice)
                .sum();

        return new OrderResponseDto(
                order.getId(),
                order.getProduct().getName(),
                orderProductDtos,
                totalPrice,
                order.getOrderStatus(),
                paymentStatus,
                order.getCreatedAt()
        );
    }
}