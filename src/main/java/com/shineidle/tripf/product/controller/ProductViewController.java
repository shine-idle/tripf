package com.shineidle.tripf.product.controller;

import com.shineidle.tripf.product.dto.ProductResponseDto;
import com.shineidle.tripf.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductViewController {

    private final ProductService productService;

    /**
     * 상품 목록 페이지를 렌더링합니다.
     *
     * @param model {@link Model} 상품 목록 데이터를 담을 모델 객체
     * @return 상품 목록 페이지 뷰 이름
     */
    @GetMapping("/products")
    public String productList(
            Model model
    ) {
        List<ProductResponseDto> products = productService.findAllProduct();
        model.addAttribute("products", products);
        return "product/productList";
    }

    /**
     * 특정 상품 상세 페이지를 렌더링합니다.
     *
     * @param productId {@link Long} 조회할 상품의 ID
     * @param model     {@link Model} 상품 데이터를 담을 모델 객체
     * @return 상품 상세 페이지 뷰 이름
     */
    @GetMapping("/products/{productId}")
    public String productDetail(
            @PathVariable Long productId,
            Model model
    ) {
        ProductResponseDto product = productService.findProduct(productId);
        model.addAttribute("product", product);
        return "product/productDetail";
    }
}