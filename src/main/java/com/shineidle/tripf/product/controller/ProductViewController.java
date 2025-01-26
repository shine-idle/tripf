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

    @GetMapping("/products")
    public String productList(Model model) {
        List<ProductResponseDto> products = productService.findAllProduct();
        model.addAttribute("products", products);
        return "product/productList";
    }

    @GetMapping("/products/{productId}")
    public String productDetail(@PathVariable Long productId, Model model) {
        ProductResponseDto product = productService.findProduct(productId);
        model.addAttribute("product", product);
        return "product/productDetail";
    }
}