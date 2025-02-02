package com.shineidle.tripf.order.dto;

import com.shineidle.tripf.order.entity.Order;
import com.shineidle.tripf.order.type.OrderStatus;
import com.shineidle.tripf.orderProduct.entity.OrderProduct;
import com.shineidle.tripf.payment.dto.PaymentRequestDto;
import com.shineidle.tripf.payment.entity.Payment;
import com.shineidle.tripf.payment.type.PaymentStatus;
import com.shineidle.tripf.product.entity.Product;
import com.shineidle.tripf.user.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

import static com.shineidle.tripf.payment.entity.QPayment.payment;

@Getter
@RequiredArgsConstructor
public class OrderRequestDto {
    @NotNull(message = "상품 ID는 필수입니다")
    private Long productId;

    @NotNull(message = "수량은 필수입니다")
    private Long quantity;

    private String paymentKey;  // 예시로 paymentKey를 받음 (추가된 부분)

    public Order toEntity(Product product, User user) {
        long totalPrice = product.getPrice() * this.quantity;

        OrderProduct orderProduct = new OrderProduct(this.quantity, product);


        Order order = new Order(
                totalPrice,
                OrderStatus.ORDER_RECEIVED,
                product,
                user,
                LocalDateTime.now(),
                null,
                null
        );
        order.addOrderProduct(orderProduct);

        // PaymentRequestDto 생성
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto(
                order.getId(),
                totalPrice,
                this.paymentKey,  // paymentKey
                PaymentStatus.PENDING  // 결제 상태는 PENDING으로 설정
        );

        // Payment 객체 생성
        Payment payment = new Payment(paymentRequestDto, order, user);
        order.setPayment(payment);  // 주문에 결제 정보 설정

        return order;
    }

}