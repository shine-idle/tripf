package com.shineidle.tripf.order.controller;

import com.shineidle.tripf.order.dto.OrderRequestDto;
import com.shineidle.tripf.order.dto.OrderResponseDto;
import com.shineidle.tripf.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    /**
     * 주문 생성
     *
     * @param dto {@link OrderRequestDto} 주문 요청 Dto
     * @return {@link OrderResponseDto} 주문 응답 Dto
     */
    @Operation(summary = "주문 생성")
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(
            @Valid @RequestBody OrderRequestDto dto
    ) {
        OrderResponseDto orderResponseDto = orderService.createOrder(dto);
        return new ResponseEntity<>(orderResponseDto, HttpStatus.CREATED);
    }

    /**
     * 주문 조회(다건)
     *
     * @return {@link OrderResponseDto} 주문 응답 Dto
     */
    @Operation(summary = "주문 다건 조회")
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> findAllOrder() {
        List<OrderResponseDto> orderResponseDtoList = orderService.findAllOrder();
        return new ResponseEntity<>(orderResponseDtoList, HttpStatus.OK);
    }

    /**
     * 주문 조회(단건)
     *
     * @param orderId 주문 식별자
     * @return {@link OrderResponseDto} 주문 응답 Dto
     */
    @Operation(summary = "주문 단건 조회")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> findOrder(
            @PathVariable Long orderId
    ) {
        OrderResponseDto orderResponseDto = orderService.findOrder(orderId);
        return new ResponseEntity<>(orderResponseDto, HttpStatus.OK);
    }
}
