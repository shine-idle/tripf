package com.shineidle.tripf.product.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminProductViewController {

    /**
     * 상품 등록 페이지 렌더링
     *
     * @param model 모델
     * @return 상품 등록 페이지
     */
    @GetMapping("/admin/products/new")
    public String createProductForm(Model model) {
        // 필요시 기본 데이터를 Model에 추가 가능
        return "product/adminProductForm"; // adminProductForm.html로 연결
    }
}