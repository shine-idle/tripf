package com.shineidle.tripf.orderProduct.entity;

import com.shineidle.tripf.common.BaseEntity;
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

    @NotNull(message = "총 가격을 입력해주세요")
    private Long purchasePrice;

   @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

   @ManyToOne(fetch = FetchType.LAZY)
   private Order order;
}
