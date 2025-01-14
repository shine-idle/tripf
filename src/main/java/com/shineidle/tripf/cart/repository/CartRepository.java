package com.shineidle.tripf.cart.repository;

import com.shineidle.tripf.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    /**
     * 유저Id와 상품Id에 해당하는 장바구니 행 반환
     *
     * @param userId 유저Id
     * @param productId 상품Id
     * @return {@link Cart}
     */
    Optional<Cart> findByUserIdAndProductId(Long userId, Long productId);

    /**
     * 유저Id에 해당하는 장바구니 리스트 반환
     *
     * @param userId 유저Id
     * @return {@link Cart}
     */
    List<Cart> findAllByUserId(Long userId);

}
