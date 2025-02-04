package com.shineidle.tripf.product.service;

import com.shineidle.tripf.common.exception.GlobalException;
import com.shineidle.tripf.common.exception.type.ProductErrorCode;
import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.common.message.type.PostMessage;
import com.shineidle.tripf.product.dto.ProductRequestDto;
import com.shineidle.tripf.product.dto.ProductResponseDto;
import com.shineidle.tripf.product.entity.Product;
import com.shineidle.tripf.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    /**
     * 상품 생성
     * @param dto {@link ProductRequestDto} </br>
     * category(카테고리), name(상품명), price(가격), description(상품 설명), stock(재고), status(상품 상태)
     * @return {@link ProductResponseDto}
     */
    @Override
    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto dto) {
        Product product = new Product(
                dto.getCategory(),
                dto.getStatus(),
                dto.getName(),
                dto.getPrice(),
                dto.getDescription(),
                dto.getStock()
        );

        productRepository.save(product);

        return ProductResponseDto.toDto(product);
    }

    /**
     * 상품 조회(다건, 단종된 상품은 제외)
     *
     * @return {@link ProductResponseDto}
     */
    @Override
    @Transactional
    public List<ProductResponseDto> findAllProduct() {
        return productRepository.findAllExceptDiscontinuedProducts()
                .stream()
                .map(ProductResponseDto::toDto)
                .toList();
    }

    /**
     * 상품 조회(단건)
     *
     * @param productId 상품Id
     * @return {@link ProductResponseDto}
     */
    @Override
    public ProductResponseDto findProduct(Long productId) {
        Product product = getProductById(productId);

        return ProductResponseDto.toDto(product);
    }

    /**
     * 상품 수정
     *
     * @param productId 상품Id
     * @param dto {@link ProductRequestDto} </br>
     * category(카테고리), name(상품명), price(가격), description(상품 설명), stock(재고), status(상품 상태)
     * @return {@link ProductResponseDto}
     */
    @Override
    @Transactional
    public ProductResponseDto updateProduct(Long productId, ProductRequestDto dto) {
        Product product = getProductById(productId);

        // 기존의 상품과 이름이 중복되는지 체크
        if (product.getName().equals(dto.getName())) {
            throw new GlobalException(ProductErrorCode.PRODUCT_DUPLICATED);
        }

        product.update(dto);
        productRepository.save(product);

        return ProductResponseDto.toDto(product);
    }

    /**
     * 상품 삭제 (단종처리)
     * @param productId 상품Id
     * @return String - 상품 삭제 처리 문구
     */
    @Override
    @Transactional
    public PostMessageResponseDto deleteProduct(Long productId) {
        Product product = getProductById(productId);

        // 상품 삭제
        product.delete();
        productRepository.save(product);

        return new PostMessageResponseDto(PostMessage.PRODUCT_DISCONTINUED);
    }

    @Override
    public Product getProductById(Long productId) {
        return productRepository.findById(productId).orElseThrow(() ->
                new GlobalException(ProductErrorCode.PRODUCT_NOT_FOUND));
    }
}
