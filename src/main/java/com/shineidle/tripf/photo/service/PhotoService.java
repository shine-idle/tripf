package com.shineidle.tripf.photo.service;

import com.shineidle.tripf.photo.dto.PhotoRequestDto;
import com.shineidle.tripf.photo.dto.PhotoResponseDto;
import com.shineidle.tripf.photo.type.PhotoDomain;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PhotoService {

    PhotoResponseDto uploadPhoto(Long domainId, PhotoRequestDto photoRequestDto, MultipartFile file, PhotoDomain domainType) throws IOException;

    PhotoResponseDto findPhoto(Long domainId, Long photoId, PhotoDomain domainType);

    List<PhotoResponseDto> findAllPhoto(Long domainId, PhotoDomain domainType);

    PhotoResponseDto updatePhoto(Long domainId, Long photoId, PhotoRequestDto photoRequestDto, MultipartFile file, PhotoDomain domainType);

    void deletePhoto(Long activityId, Long photoId, PhotoDomain domainType);

    List<String> getActivityPhotoUrls(Long activityId);
}
