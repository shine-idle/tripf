package com.shineidle.tripf.photo.controller;

import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.common.message.type.PostMessage;
import com.shineidle.tripf.photo.dto.PhotoRequestDto;
import com.shineidle.tripf.photo.dto.PhotoResponseDto;
import com.shineidle.tripf.photo.service.PhotoService;
import com.shineidle.tripf.photo.type.PhotoDomain;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/admin/products/{productId}/photos")
@RequiredArgsConstructor
public class AdminProductPhotoController {

    private final PhotoService photoService;

    /**
     * 상품 사진 업로드
     *
     * @param productId 상품 식별자
     * @param photoRequestDto {@link PhotoRequestDto} 사진 요청 Dto
     * @param file {@link MultipartFile} 첨부할 사진
     * @return {@link PhotoResponseDto} 사진 응답 Dto
     */
    @Operation(summary = "상품 사진 업로드")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<PhotoResponseDto> uploadPhotoToProduct(
            @PathVariable Long productId,
            @ModelAttribute PhotoRequestDto photoRequestDto,
            @RequestPart MultipartFile file
    ) throws IOException {

        PhotoResponseDto responseDto = photoService.uploadPhoto(productId, photoRequestDto, file, PhotoDomain.PRODUCT);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * 상품 사진 수정
     *
     * @param productId 상품 식별자
     * @param photoId 사진 식별자
     * @param photoRequestDto {@link PhotoRequestDto} 사진 요청 Dto
     * @param file {@link MultipartFile} 첨부할 사진
     * @return {@link PhotoResponseDto} 사진 응답 Dto
     */
    @Operation(summary = "상품 사진 수정")
    @RequestMapping(
            value = "/{photoId}",
            method = RequestMethod.PATCH,
            consumes = "multipart/form-data"
    )
    public ResponseEntity<PhotoResponseDto> updateProductPhoto(
            @PathVariable Long productId,
            @PathVariable Long photoId,
            @ModelAttribute PhotoRequestDto photoRequestDto,
            @RequestParam MultipartFile file
    ) {
        PhotoResponseDto responseDto = photoService.updatePhoto(productId, photoId, photoRequestDto, file, PhotoDomain.PRODUCT);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    /**
     * 상품 사진 삭제
     *
     * @param productId 상품 식별자
     * @param photoId 사진 식별자
     * @return {@link PostMessageResponseDto} 사진 삭제 문구
     */
    @Operation(summary = "상품 사진 삭제")
    @DeleteMapping("/{photoId}")
    public ResponseEntity<PostMessageResponseDto> deleteProductPhoto(
            @PathVariable Long productId,
            @PathVariable Long photoId
    ) {
        photoService.deletePhoto(productId, photoId, PhotoDomain.PRODUCT);

        PostMessageResponseDto responseDto = new PostMessageResponseDto(PostMessage.PHOTO_DELETED);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}
