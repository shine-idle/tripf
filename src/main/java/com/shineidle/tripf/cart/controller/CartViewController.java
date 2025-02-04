package com.shineidle.tripf.cart.controller;

import com.shineidle.tripf.cart.dto.CartResponseDto;
import com.shineidle.tripf.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartViewController {
    private final CartService cartService;

    /**
     * 장바구니 페이지 렌더링
     *
     * @param model 모델
     * @return 장바구니 페이지
     */
    @GetMapping("/cart")
    public String viewCart(Model model) {
        List<CartResponseDto> cartItems = cartService.findCart();
        model.addAttribute("cartItems", cartItems);
        return "cart/cartList"; // cartList.html로 연결
    }
}