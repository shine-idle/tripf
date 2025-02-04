package com.shineidle.tripf.product.service;

import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.product.dto.ProductRequestDto;
import com.shineidle.tripf.product.dto.ProductResponseDto;
import com.shineidle.tripf.product.entity.Product;

import java.util.List;

public interface ProductService {
    /**
     * 상품 생성
     *
     * @param dto 생성할 상품 정보를 담은 DTO
     * @return 생성된 상품 정보를 담은 DTO
     */
    ProductResponseDto createProduct(ProductRequestDto dto);

    /**
     * 기존 상품 정보를 업데이트
     *
     * @param productId 업데이트할 상품의 Id
     * @param dto       업데이트할 상품 정보를 담은 DTO
     * @return 업데이트된 상품 정보를 담은 DTO
     */
    ProductResponseDto updateProduct(Long productId, ProductRequestDto dto);

    /**
     * 특정 상품을 삭제
     *
     * @param productId 삭제할 상품의 Id
     * @return 삭제 결과를 담은 응답 DTO
     */
    PostMessageResponseDto deleteProduct(Long productId);

    /**
     * 모든 상품 정보를 조회
     *
     * @return 전체 상품 목록을 담은 DTO 리스트
     */
    List<ProductResponseDto> findAllProduct();

    /**
     * 특정 상품 정보를 조회
     *
     * @param productId 조회할 상품의 Id
     * @return 조회된 상품 정보를 담은 DTO
     */
    ProductResponseDto findProduct(Long productId);

    /**
     * 특정 상품 엔티티를 조회
     *
     * @param productId 조회할 상품의 Id
     * @return 조회된 상품 엔티티
     */
    Product getProductById(Long productId);
}
