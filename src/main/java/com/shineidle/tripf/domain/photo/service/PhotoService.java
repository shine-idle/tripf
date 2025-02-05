package com.shineidle.tripf.domain.photo.service;

import com.shineidle.tripf.domain.photo.dto.PhotoRequestDto;
import com.shineidle.tripf.domain.photo.dto.PhotoResponseDto;
import com.shineidle.tripf.domain.photo.type.PhotoDomain;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PhotoService {
    /**
     * 사진 업로드
     *
     * @param domainId        상품 식별자 또는 활동 식별자
     * @param photoRequestDto {@link PhotoRequestDto} 사진 요청 Dto
     * @param multipartFile   {@link MultipartFile} 첨부할 사진
     * @param domainType      {@link PhotoDomain} 상품 또는 활동
     * @return {@link PhotoResponseDto} 사진 응답 Dto
     */
    PhotoResponseDto uploadPhoto(Long domainId, PhotoRequestDto photoRequestDto, MultipartFile multipartFile, PhotoDomain domainType) throws IOException;

    /**
     * 사진 단건 조회
     *
     * @param domainId   상품 식별자 또는 활동 식별자
     * @param photoId    사진 식별자
     * @param domainType {@link PhotoDomain} 상품 또는 활동
     * @return {@link PhotoResponseDto} 사진 응답 Dto
     */
    PhotoResponseDto findPhoto(Long domainId, Long photoId, PhotoDomain domainType);

    /**
     * 사진 다건 조회
     * x
     *
     * @param domainId   상품 식별자 또는 활동 식별자
     * @param domainType {@link PhotoDomain} 상품 또는 활동
     * @return {@link PhotoResponseDto} 사진 응답 Dto
     */
    List<PhotoResponseDto> findAllPhoto(Long domainId, PhotoDomain domainType);

    /**
     * 사진 수정
     *
     * @param domainId        상품 식별자 또는 활동 식별자
     * @param photoId         사진 식별자
     * @param photoRequestDto {@link PhotoRequestDto} 사진 요청 Dto
     * @param multipartFile   {@link MultipartFile} 첨부할 사진
     * @param domainType      {@link PhotoDomain} 상품 또는 활동
     * @return {@link PhotoResponseDto} 사진 응답 Dto
     */
    PhotoResponseDto updatePhoto(Long domainId, Long photoId, PhotoRequestDto photoRequestDto, MultipartFile multipartFile, PhotoDomain domainType);

    /**
     * 사진 삭제
     *
     * @param domainId   상품 식별자 또는 활동 식별자
     * @param photoId    사진 식별자
     * @param domainType {@link PhotoDomain} 상품 또는 활동
     */
    void deletePhoto(Long domainId, Long photoId, PhotoDomain domainType);

    /**
     * 특정 활동에 연관된 사진 URL 목록 조회
     *
     * @param activityId 활동 식별자
     * @return URL 리스트 문구 반환
     */
    List<String> getActivityPhotoUrls(Long activityId);
}