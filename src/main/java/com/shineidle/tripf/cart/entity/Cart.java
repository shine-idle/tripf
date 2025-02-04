package com.shineidle.tripf.cart.entity;

import com.shineidle.tripf.cart.dto.CartRequestDto;
import com.shineidle.tripf.common.entity.BaseEntity;
import com.shineidle.tripf.product.entity.Product;
import com.shineidle.tripf.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Table(name = "`cart`")
@DynamicUpdate
public class Cart extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private Long quantity;

    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    protected Cart() {
    }

    public Cart(Long quantity, User user, Product product) {
        this.quantity = quantity;
        this.user = user;
        this.product = product;
    }

    /**
     * 연관관계 - Foreign Key 값을 따로 컬럼으로 정의하지 않고 연관 관계로 정의합니다.
     */
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     */
    public void updateCart(CartRequestDto cartRequestDto) {
        this.quantity = cartRequestDto.getQuantity();
    }
}