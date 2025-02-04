package com.shineidle.tripf.photo.controller;

import com.shineidle.tripf.photo.dto.PhotoResponseDto;
import com.shineidle.tripf.photo.service.PhotoService;
import com.shineidle.tripf.photo.type.PhotoDomain;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/photos")
@RequiredArgsConstructor
public class ProductPhotoController {

    private final PhotoService photoService;

    /**
     * 상품 사진 단건 조회
     *
     * @param productId 상품 식별자
     * @param photoId 사진 식별자
     * @return {@link PhotoResponseDto} 사진 응답 Dto
     */
    @Operation(summary = "상품 사진 단건 조회")
    @GetMapping("/{photoId}")
    public ResponseEntity<PhotoResponseDto> findProductPhoto(
            @PathVariable Long productId,
            @PathVariable Long photoId
    ) {
        PhotoResponseDto responseDto = photoService.findPhoto(productId, photoId, PhotoDomain.PRODUCT);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * 상품 사진 다건 조회
     *
     * @param productId 상품 식별자
     * @return {@link PhotoResponseDto} 사진 응답 Dto
     */
    @Operation(summary = "상품 사진 다건 조회")
    @GetMapping()
    public ResponseEntity<List<PhotoResponseDto>> findAllProductPhoto(
            @PathVariable Long productId
    ) {
        List<PhotoResponseDto> responseDtos = photoService.findAllPhoto(productId, PhotoDomain.PRODUCT);

        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }
}