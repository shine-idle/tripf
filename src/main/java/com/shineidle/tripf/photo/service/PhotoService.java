package com.shineidle.tripf.photo.service;

import com.shineidle.tripf.photo.dto.PhotoRequestDto;
import com.shineidle.tripf.photo.dto.PhotoResponseDto;
import com.shineidle.tripf.photo.type.PhotoDomain;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PhotoService {
    /**
     * 사진 업로드
     */
    PhotoResponseDto uploadPhoto(Long domainId, PhotoRequestDto photoRequestDto, MultipartFile file, PhotoDomain domainType) throws IOException;

    /**
     * 사진 조회
     */
    PhotoResponseDto findPhoto(Long domainId, Long photoId, PhotoDomain domainType);

    /**
     * 모든 사진 조회
     */
    List<PhotoResponseDto> findAllPhoto(Long domainId, PhotoDomain domainType);

    /**
     * 사진 수정
     */
    PhotoResponseDto updatePhoto(Long domainId, Long photoId, PhotoRequestDto photoRequestDto, MultipartFile file, PhotoDomain domainType);

    /**
     * 사진 삭제
     */
    void deletePhoto(Long activityId, Long photoId, PhotoDomain domainType);

    /**
     * 특정 활동에 연관된 사진 URL 목록 조회
     */
    List<String> getActivityPhotoUrls(Long activityId);
}