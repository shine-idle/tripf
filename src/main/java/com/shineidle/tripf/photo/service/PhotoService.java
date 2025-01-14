package com.shineidle.tripf.photo.service;

import com.shineidle.tripf.photo.dto.PhotoCreateRequestDto;
import com.shineidle.tripf.photo.dto.PhotoCreateResponseDto;
import com.shineidle.tripf.photo.type.PhotoDomain;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PhotoService {

    PhotoCreateResponseDto uploadPhoto(Long domainId, PhotoCreateRequestDto photoCreateRequestDto, MultipartFile file, PhotoDomain domainType) throws IOException;

    PhotoCreateResponseDto findPhoto(Long domainId, Long photoId, PhotoDomain domainType);

    List<PhotoCreateResponseDto> findAllPhoto(Long domainId, PhotoDomain domainType);

    PhotoCreateResponseDto updatePhoto(Long domainId, Long photoId, PhotoCreateRequestDto photoCreateRequestDto, MultipartFile file, PhotoDomain domainType);

    void deletePhoto(Long activityId, Long photoId, PhotoDomain domainType);
}
