package com.shineidle.tripf.order.dto;

import com.shineidle.tripf.order.entity.Order;
import com.shineidle.tripf.order.type.OrderStatus;
import com.shineidle.tripf.order.type.PayStatus;
import com.shineidle.tripf.orderProduct.dto.OrderProductDto;
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
    private final PayStatus payStatus;
    private final LocalDateTime createTime;

    public static OrderResponseDto toDto(Order order) {
        List<OrderProductDto> orderProductDtos = order.getOrderProducts().stream()
                .map(orderProduct -> new OrderProductDto(
                        orderProduct.getProduct().getName(),
                        orderProduct.getQuantity(),
                        orderProduct.getPurchasePrice(),
                        orderProduct.getQuantity() * orderProduct.getPurchasePrice()
                ))
                .collect(Collectors.toList());

        Long totalPrice = order.getOrderProducts().stream()
                .mapToLong(op -> op.getQuantity() * op.getPurchasePrice())
                .sum();

        return new OrderResponseDto(
                order.getId(),
                order.getProduct().getName(),
                orderProductDtos,
                totalPrice,
                order.getOrderStatus(),
                order.getPayStatus(),
                order.getCreatedAt()
        );
    }
}
