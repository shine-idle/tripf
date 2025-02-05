package com.shineidle.tripf.domain.cart.repository;

import com.shineidle.tripf.domain.cart.entity.Cart;
import com.shineidle.tripf.domain.product.entity.Product;
import com.shineidle.tripf.domain.product.repository.ProductRepository;
import com.shineidle.tripf.domain.product.type.ProductCategory;
import com.shineidle.tripf.domain.product.type.ProductStatus;
import com.shineidle.tripf.domain.user.entity.User;
import com.shineidle.tripf.domain.user.repository.UserRepository;
import com.shineidle.tripf.domain.user.type.UserAuth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CartRepositoryTest {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    private Cart cart;

    @BeforeEach
    void setUp() {
        User user = new User("test@example.com", "password", "User name", UserAuth.NORMAL, "Address");
        Product product = new Product(ProductCategory.GOODS, ProductStatus.IN_STOCK, "Product Name", 1000L, "Product Description", 10L);

        userRepository.save(user);
        productRepository.save(product);

        cart = new Cart(1L, user, product);

        cartRepository.save(cart);
    }

    @Test
    void findByUserIdAndProductIdSuccessTest() {
        Optional<Cart> foundCart = cartRepository.findByUserIdAndProductId(1L, 1L);

        assertTrue(foundCart.isPresent());
        assertEquals(cart.getUser().getId(), foundCart.get().getUser().getId());
        assertEquals(cart.getProduct().getId(), foundCart.get().getProduct().getId());
    }

    @Test
    void findByUserIdAndProductIdNotFoundTest() {
        Optional<Cart> foundCart = cartRepository.findByUserIdAndProductId(2L, 2L);

        assertFalse(foundCart.isPresent());
    }

    @Test
    void findAllByUserId() {
        List<Cart> cartList = cartRepository.findAllByUserId(1L);

        assertFalse(cartList.isEmpty());
        assertEquals(1, cartList.size());
        assertEquals(cart.getUser().getId(), cartList.get(0).getUser().getId());
    }

    @Test
    void findAllByUserIdNotFoundTest() {
        List<Cart> cartList = cartRepository.findAllByUserId(2L);

        assertTrue(cartList.isEmpty());
    }
}