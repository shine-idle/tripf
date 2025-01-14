package com.shineidle.tripf.cart.service;

import com.shineidle.tripf.cart.dto.CartCreateRequestDto;
import com.shineidle.tripf.cart.dto.CartCreateResponseDto;
import com.shineidle.tripf.cart.entity.Cart;
import com.shineidle.tripf.cart.repository.CartRepository;
import com.shineidle.tripf.common.exception.GlobalException;
import com.shineidle.tripf.common.exception.type.CartErrorCode;
import com.shineidle.tripf.common.exception.type.ProductErrorCode;
import com.shineidle.tripf.common.util.UserAuthorizationUtil;
import com.shineidle.tripf.product.entity.Product;
import com.shineidle.tripf.product.repository.ProductRepository;
import com.shineidle.tripf.user.entity.User;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    /**
     * 장바구니 생성
     *
     * @param productId 상품Id
     * @param cartCreateRequestDto {@link CartCreateRequestDto}
     * @return {@link CartCreateResponseDto} 생성된 장바구니 응답값
     */
    @Override
    public CartCreateResponseDto createCart(Long productId, CartCreateRequestDto cartCreateRequestDto) {

        User loginedUser = UserAuthorizationUtil.getLoginUser();
        Product foundProduct = findByIdOrElseThrow(productId);

        Cart cart = new Cart(cartCreateRequestDto.getQuantity() , loginedUser, foundProduct);
        Cart savedCart = cartRepository.save(cart);

        return CartCreateResponseDto.toDto(savedCart);
    }

    /**
     * 장바구니 조회
     *
     * @return {@link CartCreateResponseDto} 조회된 장바구니 응답값
     */
    @Override
    public List<CartCreateResponseDto> findCart() {

        Long userId = UserAuthorizationUtil.getLoginUserId();

        List<Cart> carts = cartRepository.findAllByUserId(userId);

        return carts.stream().map(CartCreateResponseDto::toDto).toList();
    }

    /**
     *
     * @param productId 상품Id
     * @param cartCreateRequestDto {@link CartCreateRequestDto}
     * @return {@link CartCreateResponseDto} 수정된 장바구니 응답값
     */
    @Override
    public CartCreateResponseDto updateCart(Long productId, CartCreateRequestDto cartCreateRequestDto) {

        Long userId = UserAuthorizationUtil.getLoginUserId();
        Product foundProduct = findByIdOrElseThrow(productId);

        // 로그인한 유저 장바구니에서 특정 상품 찾기
        Cart foundCart = findByUserIdAndProductId(userId, foundProduct.getId());

        foundCart.updateCart(cartCreateRequestDto);

        cartRepository.save(foundCart);

        return CartCreateResponseDto.toDto(foundCart);
    }

    /**
     * 장바구니 삭제
     *
     * @param productId 상품Id
     */
    @Override
    public void deleteCart(Long productId) {

        Long userId = UserAuthorizationUtil.getLoginUserId();

        Product foundProduct = findByIdOrElseThrow(productId);

        Cart foundCart = findByUserIdAndProductId(userId, foundProduct.getId());

        cartRepository.delete(foundCart);
    }

    /**
     * 상품Id로 상품 조회
     *
     * @param productId 상품Id
     * @return Product {@link Product}
     */
    public Product findByIdOrElseThrow(Long productId) {

        return productRepository.findById(productId)
                .orElseThrow(() -> new GlobalException(ProductErrorCode.PRODUCT_NOT_FOUND));
    }

    /**
     * 유저Id와 상품Id에 해당하는 장바구니 조회
     * @param userId
     * @param productId
     * @return Cart {@link Cart}
     */
    public Cart findByUserIdAndProductId(Long userId, Long productId) {

        return cartRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(()-> new GlobalException(CartErrorCode.CART_NOT_FOUND));

    }

}