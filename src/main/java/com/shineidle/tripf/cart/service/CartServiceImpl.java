package com.shineidle.tripf.cart.service;

import com.shineidle.tripf.cart.dto.CartRequestDto;
import com.shineidle.tripf.cart.dto.CartResponseDto;
import com.shineidle.tripf.cart.entity.Cart;
import com.shineidle.tripf.cart.repository.CartRepository;
import com.shineidle.tripf.common.exception.GlobalException;
import com.shineidle.tripf.common.exception.type.CartErrorCode;
import com.shineidle.tripf.common.exception.type.ProductErrorCode;
import com.shineidle.tripf.common.util.auth.UserAuthorizationUtil;
import com.shineidle.tripf.product.entity.Product;
import com.shineidle.tripf.product.repository.ProductRepository;
import com.shineidle.tripf.user.entity.User;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    /**
     * 장바구니 생성
     *
     * @param productId 상품 식별자
     * @param cartRequestDto {@link CartRequestDto} 장바구니 요청 Dto
     * @return {@link CartResponseDto} 장바구니 응답 Dto
     */
    @Override
    @Transactional
    public CartResponseDto createCart(Long productId, CartRequestDto cartRequestDto) {

        User loginedUser = UserAuthorizationUtil.getLoginUser();
        Product foundProduct = findByIdOrElseThrow(productId);

        Cart cart = new Cart(cartRequestDto.getQuantity() , loginedUser, foundProduct);
        Cart savedCart = cartRepository.save(cart);

        return CartResponseDto.toDto(savedCart);
    }

    /**
     * 장바구니 조회
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
     * 장바구니 수정
     *
     * @param productId 상품 식별자
     * @param cartRequestDto {@link CartRequestDto} 장바구니 요청 Dto
     * @return {@link CartResponseDto} 장바구니 응답 Dto
     */
    @Override
    @Transactional
    public CartResponseDto updateCart(Long productId, CartRequestDto cartRequestDto) {

        Long userId = UserAuthorizationUtil.getLoginUserId();
        Product foundProduct = findByIdOrElseThrow(productId);

        // 로그인한 유저 장바구니에서 특정 상품 찾기
        Cart foundCart = findByUserIdAndProductId(userId, foundProduct.getId());

        foundCart.updateCart(cartRequestDto);

        cartRepository.save(foundCart);

        return CartResponseDto.toDto(foundCart);
    }

    /**
     * 장바구니 삭제
     *
     * @param productId 상품 식별자
     */
    @Override
    @Transactional
    public void deleteCart(Long productId) {

        Long userId = UserAuthorizationUtil.getLoginUserId();

        Product foundProduct = findByIdOrElseThrow(productId);

        Cart foundCart = findByUserIdAndProductId(userId, foundProduct.getId());

        cartRepository.delete(foundCart);
    }

    /**
     * 상품Id로 상품 조회
     *
     * @param productId 상품 식별자
     * @return Product {@link Product}
     */
    public Product findByIdOrElseThrow(Long productId) {

        return productRepository.findById(productId)
                .orElseThrow(() -> new GlobalException(ProductErrorCode.PRODUCT_NOT_FOUND));
    }

    /**
     * 유저Id와 상품Id에 해당하는 장바구니 조회
     *
     * @param userId 유저 식별자
     * @param productId 상품 식별자
     * @return Cart {@link Cart}
     */
    public Cart findByUserIdAndProductId(Long userId, Long productId) {

        return cartRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(()-> new GlobalException(CartErrorCode.CART_NOT_FOUND));

    }

}