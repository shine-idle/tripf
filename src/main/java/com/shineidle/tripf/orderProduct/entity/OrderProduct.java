package com.shineidle.tripf.orderProduct.entity;

import com.shineidle.tripf.common.entity.BaseEntity;
import com.shineidle.tripf.common.exception.GlobalException;
import com.shineidle.tripf.common.exception.type.OrderErrorCode;
import com.shineidle.tripf.order.entity.Order;
import com.shineidle.tripf.product.entity.Product;
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

   public OrderProduct() {}

    public OrderProduct(Long quantity, Product product) {
        if (quantity <= 0) {
            throw new GlobalException(OrderErrorCode.INVALID_QUANTITY);
        }
        this.quantity = quantity;
        this.purchasePrice = product.getPrice();
        this.product = product;
    }

    public void setOrder(Order order) {
       this.order = order;
    }

    public Long getTotalPrice() {
        return this.purchasePrice * this.quantity;
    }
}
