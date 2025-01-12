package com.shineidle.tripf.order.service;

import com.shineidle.tripf.common.exception.GlobalException;
import com.shineidle.tripf.common.exception.type.OrderErrorCode;
import com.shineidle.tripf.common.util.UserAuthorizationUtil;
import com.shineidle.tripf.order.dto.OrderRequestDto;
import com.shineidle.tripf.order.dto.OrderResponseDto;
import com.shineidle.tripf.order.entity.Order;
import com.shineidle.tripf.order.repository.OrderRepository;
import com.shineidle.tripf.product.entity.Product;
import com.shineidle.tripf.product.service.ProductService;
import com.shineidle.tripf.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;

    /**
     * 주문 생성
     * @param dto
     * @return OrderResponseDto
     */
    @Override
    public OrderResponseDto createOrder(OrderRequestDto dto) {
        User user = UserAuthorizationUtil.getLoginUser();
        Product product = productService.getProductById(dto.getProductId());

        if (dto.getQuantity() <= 0) {
            throw new GlobalException(OrderErrorCode.INVALID_QUANTITY);
        }

        Order order = dto.toEntity(product, user);

        orderRepository.save(order);
        return OrderResponseDto.toDto(order);
    }

    /**
     * 주문조회(다건)
     * @return List<OrderResponseDto>
     */
    @Override
    public List<OrderResponseDto> findAllOrder() {
        User user = UserAuthorizationUtil.getLoginUser();

        List<Order> orders = orderRepository.findByUserId(user.getId());

        if (orders == null || orders.isEmpty()) {
            throw new GlobalException(OrderErrorCode.ORDER_NOT_FOUND);
        }

        return orders.stream()
                .map(OrderResponseDto::toDto)
                .collect(Collectors.toList());
    }

    /**
     * 주문조회(단건)
     * @param id 주문 ID
     * @return OrderResponseDto
     */
    @Override
    public OrderResponseDto findOrder(Long id) {
        User user = UserAuthorizationUtil.getLoginUser();

        Optional<Order> order = orderRepository.findByIdAndUserId(id, user.getId());
        return order.map(OrderResponseDto::toDto)
                .orElseThrow(() -> new GlobalException(OrderErrorCode.ORDER_NOT_FOUND));
    }
}


