package com.shineidle.tripf.domain.cart.service;

import com.shineidle.tripf.domain.cart.dto.CartRequestDto;
import com.shineidle.tripf.domain.cart.dto.CartResponseDto;
import com.shineidle.tripf.domain.cart.entity.Cart;
import com.shineidle.tripf.domain.cart.repository.CartRepository;
import com.shineidle.tripf.domain.product.service.ProductService;
import com.shineidle.tripf.global.common.exception.GlobalException;
import com.shineidle.tripf.global.common.exception.type.CartErrorCode;
import com.shineidle.tripf.global.common.util.auth.UserAuthorizationUtil;
import com.shineidle.tripf.domain.product.entity.Product;
import com.shineidle.tripf.domain.user.entity.User;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final ProductService productService;

    /**
     * 장바구니 생성
     *
     * @param productId      상품 식별자
     * @param cartRequestDto {@link CartRequestDto} 장바구니 요청 Dto
     * @return {@link CartResponseDto} 장바구니 응답 Dto
     */
    @Override
    @Transactional
    public CartResponseDto createCart(Long productId, CartRequestDto cartRequestDto) {
        User loginedUser = UserAuthorizationUtil.getLoginUser();
        Product foundProduct = productService.getProductById(productId);

        Cart cart = new Cart(cartRequestDto.getQuantity(), loginedUser, foundProduct);
        Cart savedCart = cartRepository.save(cart);

        return CartResponseDto.toDto(savedCart);
    }

    /**
     * 장바구니 목록 조회
     *
     * @return {@link CartResponseDto} 장바구니 응답 Dto
     */
    @Override
    @Transactional
    public List<CartResponseDto> findCart() {
        Long userId = UserAuthorizationUtil.getLoginUserId();

        List<Cart> carts = cartRepository.findAllByUserId(userId);

        return carts.stream().map(CartResponseDto::toDto).toList();
    }

    /**
     * 장바구니에 담긴 상품 수량 수정
     *
     * @param productId      상품 식별자
     * @param cartRequestDto {@link CartRequestDto} 장바구니 요청 Dto
     * @return {@link CartResponseDto} 장바구니 응답 Dto
     */
    @Override
    @Transactional
    public CartResponseDto updateCart(Long productId, CartRequestDto cartRequestDto) {
        Long userId = UserAuthorizationUtil.getLoginUserId();

        Product foundProduct = productService.getProductById(productId);
        Cart foundCart = findByUserIdAndProductId(userId, foundProduct.getId());
        foundCart.updateCart(cartRequestDto);

        cartRepository.save(foundCart);

        return CartResponseDto.toDto(foundCart);
    }

    /**
     * 장바구니 상품 삭제
     *
     * @param productId 상품 식별자
     */
    @Override
    @Transactional
    public void deleteCart(Long productId) {
        Long userId = UserAuthorizationUtil.getLoginUserId();

        Product foundProduct = productService.getProductById(productId);
        Cart foundCart = findByUserIdAndProductId(userId, foundProduct.getId());

        cartRepository.delete(foundCart);
    }

    /**
     * 유저Id와 상품Id에 해당하는 장바구니 조회
     *
     * @param userId    유저 식별자
     * @param productId 상품 식별자
     * @return Cart {@link Cart}
     */
    public Cart findByUserIdAndProductId(Long userId, Long productId) {
        return cartRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new GlobalException(CartErrorCode.CART_NOT_FOUND));
    }
}