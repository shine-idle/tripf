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
import com.shineidle.tripf.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public CartCreateResponseDto createCart(Long productId, CartCreateRequestDto cartCreateRequestDto) {

        Long userId = UserAuthorizationUtil.getLoginUserId();
        User foundUser = userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."));

        Product foundProduct = findByIdOrElseThrow(productId);

        Cart cart = new Cart(cartCreateRequestDto.getQuantity() , foundUser, foundProduct);
        Cart savedCart = cartRepository.save(cart);

        return CartCreateResponseDto.toDto(savedCart);
    }

    @Override
    public List<CartCreateResponseDto> findCart() {

        Long userId = UserAuthorizationUtil.getLoginUserId();

        List<Cart> carts = cartRepository.findAllByUserId(userId);

        return carts.stream().map(CartCreateResponseDto::toDto).toList();
    }

    @Override
    public CartCreateResponseDto updateCart(Long productId, CartCreateRequestDto cartCreateRequestDto) {

        Long userId = UserAuthorizationUtil.getLoginUserId();
        Product foundProduct = findByIdOrElseThrow(productId);

        // 로그인한 유저 장바구니에서 특정 상품 찾기
        Cart foundCart = findByUserIdAndProductId(userId, productId);

        foundCart.updateCart(cartCreateRequestDto);

        cartRepository.save(foundCart);

        return CartCreateResponseDto.toDto(foundCart);
    }

    @Override
    public void deleteCart(Long productId) {

        Long userId = UserAuthorizationUtil.getLoginUserId();

        Product foundProduct = findByIdOrElseThrow(productId);

        Cart foundCart = findByUserIdAndProductId(userId, productId);

        cartRepository.delete(foundCart);
    }

//    public Product findByIdOrElseThrow(Long productId) {
//
//        return productRepository.findById(productId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 상품을 찾을 수 없습니다."));
//    }

    public Product findByIdOrElseThrow(Long productId) {

        return productRepository.findById(productId)
                .orElseThrow(() -> new GlobalException(ProductErrorCode.PRODUCT_NOT_FOUND));
    }

//    public Cart findByUserIdAndProductId(Long userId, Long productId) {
//
//        return cartRepository.findByUserIdAndProductId(userId, productId)
//                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 유저와 상품을 찾을 수 없습니다."));
//
//    }

    public Cart findByUserIdAndProductId(Long userId, Long productId) {

        return cartRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(()-> new GlobalException(CartErrorCode.CART_NOT_FOUND));

    }

}