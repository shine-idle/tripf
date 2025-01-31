package com.shineidle.tripf.cart.service;

import com.shineidle.tripf.cart.dto.CartRequestDto;
import com.shineidle.tripf.cart.dto.CartResponseDto;
import com.shineidle.tripf.cart.entity.Cart;
import com.shineidle.tripf.cart.repository.CartRepository;
import com.shineidle.tripf.common.exception.GlobalException;
import com.shineidle.tripf.common.exception.type.CartErrorCode;
import com.shineidle.tripf.common.exception.type.ProductErrorCode;
import com.shineidle.tripf.common.util.UserAuthorizationUtil;
import com.shineidle.tripf.product.entity.Product;
import com.shineidle.tripf.product.repository.ProductRepository;
import com.shineidle.tripf.product.type.ProductCategory;
import com.shineidle.tripf.product.type.ProductStatus;
import com.shineidle.tripf.user.entity.User;
import com.shineidle.tripf.user.type.UserAuth;
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

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private User user;
    private Product product;
    private Cart cart;

    @BeforeEach
    void setUp() {
        // ✅ User 객체 초기화
        user = new User("test@example.com",
                "password123",
                "testUser",
                UserAuth.NORMAL,
                "123 Street, City"
        );

        // ✅ Product 객체 초기화
        product = new Product(ProductCategory.GOODS,
                ProductStatus.IN_STOCK, "Test Product",
                10000L,
                "Test Description",
                10L
        );

        // ✅ 리플렉션을 사용하여 Product ID 강제 설정
        try {
            Field idField = Product.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(product, 1L); // ID 값 설정
        } catch (Exception e) {
            throw new RuntimeException("Product ID 설정 실패", e);
        }

        // ✅ Cart 객체 초기화
        cart = new Cart(2L, user, product);

        // ✅ 검증: Product의 ID가 null이 아닌지 확인
        assertNotNull(product.getId(), "Product ID가 null입니다!");
    }

    @Test
    void createCart_Success() {
        CartRequestDto requestDto = new CartRequestDto(2L);

        try (MockedStatic<UserAuthorizationUtil> mockedUserAuth = mockStatic(UserAuthorizationUtil.class)) {
            mockedUserAuth.when(UserAuthorizationUtil::getLoginUser).thenReturn(user);
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(cartRepository.save(any(Cart.class))).thenReturn(cart);

            CartResponseDto responseDto = cartService.createCart(1L, requestDto);

            assertThat(responseDto).isNotNull();
            assertThat(responseDto.getQuantity()).isEqualTo(2);
            verify(cartRepository, times(1)).save(any(Cart.class));
        }
    }

    @Test
    void createCart_ProductNotFound() {
        CartRequestDto requestDto = new CartRequestDto(2L);

        try (MockedStatic<UserAuthorizationUtil> mockedUserAuth = mockStatic(UserAuthorizationUtil.class)) {
            mockedUserAuth.when(UserAuthorizationUtil::getLoginUser).thenReturn(user);
            when(productRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> cartService.createCart(1L, requestDto))
                    .isInstanceOf(GlobalException.class)
                    .hasMessage(ProductErrorCode.PRODUCT_NOT_FOUND.getMessage());
        }
    }

    @Test
    void findCart_Success() {
        try (MockedStatic<UserAuthorizationUtil> mockedUserAuth = mockStatic(UserAuthorizationUtil.class)) {
            mockedUserAuth.when(UserAuthorizationUtil::getLoginUserId).thenReturn(1L);
            when(cartRepository.findAllByUserId(1L)).thenReturn(List.of(cart));

            List<CartResponseDto> cartList = cartService.findCart();

            // ✅ 타입을 명확하게 지정하여 ListAssert로 해석되도록 함
            assertThat(cartList).asList().isNotEmpty();

            // ✅ 첫 번째 요소의 클래스 타입 검증
            assertThat(cartList.get(0)).isInstanceOf(CartResponseDto.class);

            // ✅ 장바구니 수량 검증
            assertThat(cartList.get(0).getQuantity()).isEqualTo(2);
        }
    }

    @Test
    void updateCart_Success() {
        CartRequestDto requestDto = new CartRequestDto(5L);

        try (MockedStatic<UserAuthorizationUtil> mockedUserAuth = mockStatic(UserAuthorizationUtil.class)) {
            mockedUserAuth.when(UserAuthorizationUtil::getLoginUserId).thenReturn(1L);

            // ✅ productId가 null이 되지 않도록 올바른 product 반환 설정
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));

            // ✅ product의 id가 null이 아닌지 검증
            assertNotNull(product.getId(), "Product ID가 null입니다!");

            // ✅ cartRepository가 올바르게 작동하도록 설정
            when(cartRepository.findByUserIdAndProductId(1L, product.getId())).thenReturn(Optional.of(cart));

            CartResponseDto responseDto = cartService.updateCart(1L, requestDto);

            assertThat(responseDto).isNotNull();
            assertThat(responseDto.getQuantity()).isEqualTo(5);
        }
    }

    @Test
    void updateCart_CartNotFound() {
        CartRequestDto requestDto = new CartRequestDto(5L);

        try (MockedStatic<UserAuthorizationUtil> mockedUserAuth = mockStatic(UserAuthorizationUtil.class)) {
            mockedUserAuth.when(UserAuthorizationUtil::getLoginUserId).thenReturn(1L);
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(cartRepository.findByUserIdAndProductId(1L, 1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> cartService.updateCart(1L, requestDto))
                    .isInstanceOf(GlobalException.class)
                    .hasMessage(CartErrorCode.CART_NOT_FOUND.getMessage());
        }
    }

    @Test
    void deleteCart_Success() {
        try (MockedStatic<UserAuthorizationUtil> mockedUserAuth = mockStatic(UserAuthorizationUtil.class)) {
            mockedUserAuth.when(UserAuthorizationUtil::getLoginUserId).thenReturn(1L);
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(cartRepository.findByUserIdAndProductId(1L, 1L)).thenReturn(Optional.of(cart));

            cartService.deleteCart(1L);

            verify(cartRepository, times(1)).delete(cart);
        }
    }

    @Test
    void deleteCart_CartNotFound() {
        try (MockedStatic<UserAuthorizationUtil> mockedUserAuth = mockStatic(UserAuthorizationUtil.class)) {
            mockedUserAuth.when(UserAuthorizationUtil::getLoginUserId).thenReturn(1L);
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(cartRepository.findByUserIdAndProductId(1L, 1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> cartService.deleteCart(1L))
                    .isInstanceOf(GlobalException.class)
                    .hasMessage(CartErrorCode.CART_NOT_FOUND.getMessage());
        }
    }
}