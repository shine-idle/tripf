package com.shineidle.tripf.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminProductViewController {

    /**
     * 상품 등록 페이지를 렌더링합니다.
     *
     * @param model {@link Model} 모델 객체
     * @return 상품 등록 페이지의 뷰 이름
     */
    @GetMapping("/admin/products/new")
    public String createProductForm(
            Model model
    ) {
        return "product/adminProductForm";
    }
}