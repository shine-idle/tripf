package com.shineidle.tripf.domain.cart.service;

import com.shineidle.tripf.domain.cart.dto.CartRequestDto;
import com.shineidle.tripf.domain.cart.dto.CartResponseDto;
import com.shineidle.tripf.domain.cart.entity.Cart;
import com.shineidle.tripf.domain.cart.repository.CartRepository;
import com.shineidle.tripf.domain.product.service.ProductServiceImpl;
import com.shineidle.tripf.global.common.exception.GlobalException;
import com.shineidle.tripf.global.common.exception.type.CartErrorCode;
import com.shineidle.tripf.global.common.exception.type.ProductErrorCode;
import com.shineidle.tripf.global.common.util.auth.UserAuthorizationUtil;
import com.shineidle.tripf.domain.product.entity.Product;
import com.shineidle.tripf.domain.product.type.ProductCategory;
import com.shineidle.tripf.domain.product.type.ProductStatus;
import com.shineidle.tripf.domain.user.entity.User;
import com.shineidle.tripf.domain.user.type.UserAuth;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {
    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Mock
    private ProductServiceImpl productService;

    private User user;
    private Product product;
    private Cart cart;

    @BeforeEach
    void setUp() {
        user = new User("test@example.com", "password123", "testUser", UserAuth.NORMAL, "123 Street, City");
        product = new Product(ProductCategory.GOODS, ProductStatus.IN_STOCK, "Test Product", 10000L, "Test Description", 10L);

        try {
            Field idField = Product.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(product, 1L);
        } catch (Exception e) {
            throw new RuntimeException("Product ID 설정 실패", e);
        }

        cart = new Cart(2L, user, product);
        assertNotNull(product.getId(), "Product ID가 null입니다!");
    }

    @Test
    void createCartSuccessTest() {
        // Given
        CartRequestDto requestDto = new CartRequestDto(2L);

        try (MockedStatic<UserAuthorizationUtil> mockedUserAuth = mockStatic(UserAuthorizationUtil.class)) {
            mockedUserAuth.when(UserAuthorizationUtil::getLoginUser).thenReturn(user);
            when(productService.getProductById(1L)).thenReturn(product);
            when(cartRepository.save(any(Cart.class))).thenReturn(cart);

            // When
            CartResponseDto responseDto = cartService.createCart(1L, requestDto);

            // Then
            assertThat(responseDto).isNotNull();
            assertThat(responseDto.getQuantity()).isEqualTo(2);
            verify(cartRepository, times(1)).save(any(Cart.class));
        }
    }

    @Test
    void createCartProductNotFoundTest() {
        // Given
        CartRequestDto requestDto = new CartRequestDto(2L);

        try (MockedStatic<UserAuthorizationUtil> mockedUserAuth = mockStatic(UserAuthorizationUtil.class)) {
            mockedUserAuth.when(UserAuthorizationUtil::getLoginUser).thenReturn(user);
            when(productService.getProductById(1L)).thenThrow(new GlobalException(ProductErrorCode.PRODUCT_NOT_FOUND));

            // When
            GlobalException exception = Assertions.assertThrows(GlobalException.class,
                    () -> cartService.createCart(1L, requestDto));

            // Then
            assertThat(exception.getErrCode()).isEqualTo(ProductErrorCode.PRODUCT_NOT_FOUND.getErrCode());
            assertThat(exception.getMessage()).isEqualTo(ProductErrorCode.PRODUCT_NOT_FOUND.getMessage());
        }
    }

    @Test
    void findCartSuccessTest() {
        // Given
        try (MockedStatic<UserAuthorizationUtil> mockedUserAuth = mockStatic(UserAuthorizationUtil.class)) {
            mockedUserAuth.when(UserAuthorizationUtil::getLoginUserId).thenReturn(1L);
            when(cartRepository.findAllByUserId(1L)).thenReturn(List.of(cart));

            // When
            List<CartResponseDto> cartList = cartService.findCart();

            // Then
            assertThat(cartList).asList().isNotEmpty();
            assertThat(cartList.get(0)).isInstanceOf(CartResponseDto.class);
            assertThat(cartList.get(0).getQuantity()).isEqualTo(2);
        }
    }

    @Test
    void updateCartSuccessTest() {
        // Given
        CartRequestDto requestDto = new CartRequestDto(5L);

        try (MockedStatic<UserAuthorizationUtil> mockedUserAuth = mockStatic(UserAuthorizationUtil.class)) {
            mockedUserAuth.when(UserAuthorizationUtil::getLoginUserId).thenReturn(1L);
            when(productService.getProductById(1L)).thenReturn(product);
            when(cartRepository.findByUserIdAndProductId(1L, product.getId())).thenReturn(Optional.of(cart));

            // When
            CartResponseDto responseDto = cartService.updateCart(1L, requestDto);

            // Then
            assertThat(responseDto).isNotNull();
            assertThat(responseDto.getQuantity()).isEqualTo(5);
        }
    }

    @Test
    void updateCartCartNotFoundTest() {
        // Given
        CartRequestDto requestDto = new CartRequestDto(5L);

        try (MockedStatic<UserAuthorizationUtil> mockedUserAuth = mockStatic(UserAuthorizationUtil.class)) {
            mockedUserAuth.when(UserAuthorizationUtil::getLoginUserId).thenReturn(1L);
            when(productService.getProductById(1L)).thenReturn(product);
            when(cartRepository.findByUserIdAndProductId(1L, 1L)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> cartService.updateCart(1L, requestDto))
                    .isInstanceOf(GlobalException.class)
                    .hasMessage(CartErrorCode.CART_NOT_FOUND.getMessage());
        }
    }

    @Test
    void deleteCartSuccessTest() {
        // Given
        try (MockedStatic<UserAuthorizationUtil> mockedUserAuth = mockStatic(UserAuthorizationUtil.class)) {
            mockedUserAuth.when(UserAuthorizationUtil::getLoginUserId).thenReturn(1L);
            when(productService.getProductById(1L)).thenReturn(product);
            when(cartRepository.findByUserIdAndProductId(1L, 1L)).thenReturn(Optional.of(cart));

            // When
            cartService.deleteCart(1L);

            // Then
            verify(cartRepository, times(1)).delete(cart);
        }
    }
}