package com.shineidle.tripf.product.service;

import com.shineidle.tripf.common.exception.GlobalException;
import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.common.message.type.PostMessage;
import com.shineidle.tripf.product.dto.ProductRequestDto;
import com.shineidle.tripf.product.dto.ProductResponseDto;
import com.shineidle.tripf.product.entity.Product;
import com.shineidle.tripf.product.repository.ProductRepository;
import com.shineidle.tripf.product.type.ProductCategory;
import com.shineidle.tripf.product.type.ProductStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product product;
    private ProductRequestDto productRequestDto;


    @BeforeEach
    void setUp() {
        product = new Product(ProductCategory.GOODS, ProductStatus.IN_STOCK, "Smartphone", 1000L, "Latest model", 50L);
        productRequestDto = new ProductRequestDto(ProductCategory.GOODS, "Smartphone", 1000L, "Latest model", 1000L, ProductStatus.IN_STOCK);
    }

    @Test
    void createProductTest() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponseDto response = productService.createProduct(productRequestDto);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(product.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void findAllProductTest() {
        when(productRepository.findAllExceptDiscontinuedProducts()).thenReturn(List.of(product));

        List<ProductResponseDto> products = productService.findAllProduct();

        assertThat(products).hasSize(1);
        assertThat(products.get(0).getName()).isEqualTo(product.getName());
        verify(productRepository, times(1)).findAllExceptDiscontinuedProducts();
    }

    @Test
    void findProductTest() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        ProductResponseDto response = productService.findProduct(1L);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(product.getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void findProduct_NotFoundTest() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(GlobalException.class, () -> productService.findProduct(1L));
    }

    @Test
    void updateProductTest() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        Product updateProduct = new Product(ProductCategory.TRAVEL, ProductStatus.IN_STOCK, "Computer", 320000L, "new Computer", 150L);
        when(productRepository.save(any(Product.class))).thenReturn(updateProduct);

        ProductRequestDto updateProductRequestDto = new ProductRequestDto(ProductCategory.TRAVEL, "Computer", 320000L, "new Computer", 150L, ProductStatus.IN_STOCK);
        ProductResponseDto response = productService.updateProduct(1L, updateProductRequestDto);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo(product.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void updateProduct_DuplicatedNameTest() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        assertThrows(GlobalException.class, () -> productService.updateProduct(1L, productRequestDto));
    }

    @Test
    void deleteProductTest() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        PostMessageResponseDto response = productService.deleteProduct(1L);

        assertThat(response.getMessage()).isEqualTo(PostMessage.PRODUCT_DISCONTINUED.getMessage());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void getProductByIdTest() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        Product foundProduct = productService.getProductById(1L);

        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getName()).isEqualTo(product.getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void getProductById_NotFoundTest() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(GlobalException.class, () -> productService.getProductById(1L));
    }
}