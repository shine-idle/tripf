package com.shineidle.tripf.domain.orderProduct.entity;

import com.shineidle.tripf.global.common.entity.BaseEntity;
import com.shineidle.tripf.global.common.exception.GlobalException;
import com.shineidle.tripf.global.common.exception.type.OrderErrorCode;
import com.shineidle.tripf.domain.order.entity.Order;
import com.shineidle.tripf.domain.product.entity.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Entity
@Table(name = "`orderProduct`")
public class OrderProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull(message = "상품 수량을 입력해주세요")
    private Long quantity;

    @NotNull(message = "총 가격을 입력해주세요")
    private Long purchasePrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    public OrderProduct() {
    }

    public OrderProduct(Long quantity, Product product) {
        if (quantity <= 0) {
            throw new GlobalException(OrderErrorCode.INVALID_QUANTITY);
        }
        this.quantity = quantity;
        this.purchasePrice = product.getPrice();
        this.product = product;
    }

    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     */
    public void updateOrder(Order order) {
        this.order = order;
    }

    public Long getTotalPrice() {
        return this.purchasePrice * this.quantity;
    }
}