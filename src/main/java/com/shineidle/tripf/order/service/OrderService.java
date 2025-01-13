package com.shineidle.tripf.order.service;

import com.shineidle.tripf.order.dto.OrderRequestDto;
import com.shineidle.tripf.order.dto.OrderResponseDto;
import jakarta.validation.Valid;

import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder (@Valid OrderRequestDto dto);
    List<OrderResponseDto> findAllOrder();
    OrderResponseDto findOrder(Long orderId);
}
