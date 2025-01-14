package com.shineidle.tripf.photo.controller;

import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.common.message.enums.PostMessage;
import com.shineidle.tripf.photo.dto.PhotoCreateRequestDto;
import com.shineidle.tripf.photo.dto.PhotoCreateResponseDto;
import com.shineidle.tripf.photo.service.PhotoService;
import com.shineidle.tripf.photo.type.PhotoDomain;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
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
     * @param productId 상품Id
     * @param photoCreateRequestDto {@link PhotoCreateRequestDto}
     * @param file {@link MultipartFile}
     * @return {@link PhotoCreateResponseDto} 생성된 사진 응답값
     */
    @PostMapping
    public ResponseEntity<PhotoCreateResponseDto> uploadPhotoToProduct(
            @PathVariable Long productId,
            @ModelAttribute PhotoCreateRequestDto photoCreateRequestDto,
            @RequestPart MultipartFile file
    ) throws IOException {

        PhotoCreateResponseDto responseDto = photoService.uploadPhoto(productId, photoCreateRequestDto, file, PhotoDomain.PRODUCT);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * 상품 사진 수정
     *
     * @param productId 상품Id
     * @param photoId 사진Id
     * @param photoCreateRequestDto {@link PhotoCreateRequestDto}
     * @param file {@link MultipartFile}
     * @return {@link PhotoCreateResponseDto} 수정된 사진 응답값
     */
    @PatchMapping("/{photoId}")
    public ResponseEntity<PhotoCreateResponseDto> updateProductPhoto(
            @PathVariable Long productId,
            @PathVariable Long photoId,
            @ModelAttribute PhotoCreateRequestDto photoCreateRequestDto,
            @RequestParam MultipartFile file
    ) {
        PhotoCreateResponseDto responseDto = photoService.updatePhoto(productId, photoId, photoCreateRequestDto, file, PhotoDomain.PRODUCT);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    /**
     * 상품 사진 삭제
     *
     * @param productId 상품Id
     * @param photoId 사진Id
     * @return {@link PostMessageResponseDto} 사진 삭제 문구
     */
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
