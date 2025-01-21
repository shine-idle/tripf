package com.shineidle.tripf.cart.entity;

import com.shineidle.tripf.cart.dto.CartRequestDto;
import com.shineidle.tripf.common.BaseEntity;
import com.shineidle.tripf.product.entity.Product;
import com.shineidle.tripf.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "`cart`")
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    protected Cart() {}

    public Cart(Long quantity, User user, Product product) {
        this.quantity = quantity;
        this.user = user;
        this.product = product;
    }

    public void updateCart(CartRequestDto cartRequestDto) {
        this.quantity = cartRequestDto.getQuantity();
    }
}
