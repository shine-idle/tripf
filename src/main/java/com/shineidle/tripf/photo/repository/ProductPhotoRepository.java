package com.shineidle.tripf.photo.repository;

import com.shineidle.tripf.photo.entity.ProductPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductPhotoRepository extends JpaRepository<ProductPhoto, Long> {

    /**
     * 상품Id와 사진Id에 해당하는 상품사진 반환
     *
     * @param productId 상품 식별자
     * @param photoId 사진 식별자
     * @return {@link ProductPhoto} 상품 사진
     */
    Optional<ProductPhoto> findByProductIdAndPhotoId(Long productId, Long photoId);

    /**
     * 상품Id에 해당하는 모든 상품사진을 List 형태로 반환
     *
     * @param productId 상품 식별자
     * @return {@link ProductPhoto} 상품 사진 리스트
     */
    List<ProductPhoto> findAllByProductId(Long productId);
}
