package com.shineidle.tripf.cart.repository;

import com.shineidle.tripf.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserIdAndProductId(Long userId, Long productId);

    List<Cart> findAllByUserId(Long userId);

}
