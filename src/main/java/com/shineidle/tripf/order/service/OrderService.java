package com.shineidle.tripf.order.service;

import com.shineidle.tripf.order.dto.OrderRequestDto;
import com.shineidle.tripf.order.dto.OrderResponseDto;
import jakarta.validation.Valid;

import java.util.List;

public interface OrderService {
    /**
     * 주문 생성
     */
    OrderResponseDto createOrder(@Valid OrderRequestDto dto);

    /**
     * 주문 조회(다건)
     */
    List<OrderResponseDto> findAllOrder();

    /**
     * 주문 조회(단건)
     */
    OrderResponseDto findOrder(Long orderId);
}