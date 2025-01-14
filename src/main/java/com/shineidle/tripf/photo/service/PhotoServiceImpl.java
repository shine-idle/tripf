package com.shineidle.tripf.photo.service;

import com.shineidle.tripf.common.exception.GlobalException;
import com.shineidle.tripf.common.exception.type.FeedErrorCode;
import com.shineidle.tripf.common.exception.type.PhotoErrorCode;
import com.shineidle.tripf.common.exception.type.ProductErrorCode;
import com.shineidle.tripf.feed.entity.Activity;
import com.shineidle.tripf.feed.repository.ActivityRepository;
import com.shineidle.tripf.photo.dto.PhotoCreateRequestDto;
import com.shineidle.tripf.photo.dto.PhotoCreateResponseDto;
import com.shineidle.tripf.photo.entity.ActivityPhoto;
import com.shineidle.tripf.photo.entity.Photo;
import com.shineidle.tripf.photo.entity.ProductPhoto;
import com.shineidle.tripf.photo.repository.ActivityPhotoRepository;
import com.shineidle.tripf.photo.repository.PhotoRepository;
import com.shineidle.tripf.photo.repository.ProductPhotoRepository;
import com.shineidle.tripf.photo.type.PhotoDomain;
import com.shineidle.tripf.product.entity.Product;
import com.shineidle.tripf.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final PhotoRepository photoRepository;
    private final ActivityRepository activityRepository;
    private final ProductRepository productRepository;
    private final ActivityPhotoRepository activityPhotoRepository;
    private final ProductPhotoRepository productPhotoRepository;
    private final S3Client s3Client;

    private static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png", "avif");
    private static final long MAX_FILE_SIZE = 8 * 1024 * 1024; // 10MB (파일 크기 제한)

    @Value("${cloud.aws.s3.bucketName}")
    private String bucket;

    /**
     * 사진 업로드
     *
     * @param domainId 상품Id 또는 활동Id
     * @param photoCreateRequestDto {@link PhotoCreateRequestDto}
     * @param multipartFile {@link MultipartFile}
     * @param domainType {@link PhotoDomain}
     * @return {@link PhotoCreateResponseDto} 생성된 사진 응답값
     */
    @Override
    @Transactional
    public PhotoCreateResponseDto uploadPhoto(Long domainId, PhotoCreateRequestDto photoCreateRequestDto, MultipartFile multipartFile, PhotoDomain domainType) throws IOException {

        // 사진 유효성 검사
        validateMultipartFile(multipartFile);

        // S3에 사진 업로드
        String storedFileName = generateUniqueFileName(multipartFile.getOriginalFilename());
        uploadToS3(multipartFile, storedFileName);

        // S3 URL 생성
        String fileUrl = generateFileUrl(storedFileName);

        // DB에 사진 정보 저장
        Photo photo = saveImage(multipartFile, photoCreateRequestDto, fileUrl);

        // 도메인에 따라 관계 엔티티 저장
        switch (domainType) {
            case ACTIVITY -> {
                Activity activity = activityRepository.findById(domainId)
                        .orElseThrow(() -> new GlobalException(FeedErrorCode.ACTIVITY_NOT_FOUND));
                ActivityPhoto activityPhoto = new ActivityPhoto(activity, photo);
                activityPhotoRepository.save(activityPhoto);
            }
            case PRODUCT -> {
                Product product = productRepository.findById(domainId)
                        .orElseThrow(() -> new GlobalException(ProductErrorCode.PRODUCT_NOT_FOUND));
                ProductPhoto productPhoto = new ProductPhoto(product, photo);
                productPhotoRepository.save(productPhoto);
            }
            default -> throw new GlobalException(PhotoErrorCode.DOMAIN_NOT_FOUND);
        }

        return new PhotoCreateResponseDto(domainId, photo.getOriginalFileName(), photo.getDescription(), fileUrl, photo.getSize(), photo.getExt(), photo.getCreatedAt(), photo.getModifiedAt());
    }

    /**
     * 사진 정보를 Photo DB에 저장
     *
     * @param multipartFile {@link MultipartFile}
     * @param photoCreateRequestDto {@link PhotoCreateRequestDto}
     * @param fileUrl 사진 경로
     * @return {@link Photo}
     */
    private Photo saveImage(MultipartFile multipartFile, PhotoCreateRequestDto photoCreateRequestDto, String fileUrl) {

        // 원본 사진 이름
        String originalFileName = multipartFile.getOriginalFilename();

        // 고유 사진 이름
        String storedFileName = generateUniqueFileName(originalFileName);

        String ext = getExtension(originalFileName);
        Long size = multipartFile.getSize();

        Photo photo = new Photo(multipartFile.getOriginalFilename(), storedFileName, photoCreateRequestDto.getDescription(), fileUrl, size, ext);
        return photoRepository.save(photo);
    }

    /**
     * 고유한 사진 이름 생성
     *
     * @param originalFileName 원본 사진 이름
     * @return String
     */
    private String generateUniqueFileName(String originalFileName) {

        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return "photo/" + UUID.randomUUID().toString() + extension;
    }

    /**
     * S3에 사진 업로드
     *
     * @param multipartFile {@link MultipartFile}
     * @param storedFileName 저장 사진 이름
     */
    private void uploadToS3(MultipartFile multipartFile, String storedFileName) throws IOException {

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(storedFileName)
                    .contentType(multipartFile.getContentType())
                    .contentLength(multipartFile.getSize())
                    .build();

            s3Client.putObject(request, RequestBody.fromBytes(multipartFile.getBytes()));

        } catch (Exception e) {
            throw new IOException("파일 업로드에 실패했습니다.", e);
        }
    }

    /**
     * S3 사진 URL 생성
     *
     * @param storedFileName 저장 사진 이름
     * @return String
     */
    private String generateFileUrl(String storedFileName) {

        return s3Client.utilities()
                .getUrl(builder -> builder.bucket(bucket).key(storedFileName)).toString();
    }

    /**
     * 사진 유효성 검증
     *
     * @param multipartFile {@link MultipartFile}
     */
    private void validateMultipartFile(MultipartFile multipartFile) {

        // 사진 존재 여부 검증
        if (multipartFile == null) {
            throw new GlobalException(PhotoErrorCode.PHOTO_EMPTY);
        }
        if (multipartFile.isEmpty()) {
            throw new GlobalException(PhotoErrorCode.PHOTO_EMPTY);
        }

        // 사진 확장자 검증
        String originalFileName = multipartFile.getOriginalFilename();
        String ext = getExtension(originalFileName);
        if (!ALLOWED_EXTENSIONS.contains(ext.toLowerCase())) {
            throw new GlobalException(PhotoErrorCode.INVALID_FILE_EXTENSION);
        }

        // 사진 크기 검증
        long fileSize = multipartFile.getSize();
        if (fileSize > MAX_FILE_SIZE) {
            throw new GlobalException(PhotoErrorCode.INVALID_FILE_SIZE);
        }
    }

    /**
     * 사진 확장자 추출
     *
     * @param originalFileName 원본 사진 이름
     * @return String
     */
    private String getExtension(String originalFileName) {

        return originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
    }

    /**
     * 사진 단건 조회
     *
     * @param domainId 상품Id 또는 활동Id
     * @param photoId 사진Id
     * @param domainType {@link PhotoDomain}
     * @return {@link PhotoCreateResponseDto} 조회된 사진 응답값
     */
    @Override
    public PhotoCreateResponseDto findPhoto(Long domainId, Long photoId, PhotoDomain domainType) {

        findByPhotoId(photoId);

        // 도메인 관계 검증
        validateRelation(domainId, photoId, domainType);

        // 연관된 사진 조회
        Photo photo = switch (domainType) {
            case ACTIVITY -> activityPhotoRepository.findByActivityIdAndPhotoId(domainId, photoId)
                    .orElseThrow(() -> new GlobalException(PhotoErrorCode.RELATION_INVALID)).getPhoto();
            case PRODUCT -> productPhotoRepository.findByProductIdAndPhotoId(domainId, photoId)
                    .orElseThrow(() -> new GlobalException(PhotoErrorCode.RELATION_INVALID)).getPhoto();
            default -> throw new GlobalException(PhotoErrorCode.DOMAIN_NOT_FOUND);
        };

        return PhotoCreateResponseDto.toDto(photo);
    }

    /**
     * 사진 다건 조회
     *
     * @param domainId 상품Id 또는 활동Id
     * @param domainType {@link PhotoDomain}
     * @return {@link PhotoCreateResponseDto} 조회된 사진 응답값
     */
    @Override
    public List<PhotoCreateResponseDto> findAllPhoto(Long domainId, PhotoDomain domainType) {

        // 연관된 사진 조회
        List<Photo> photos = switch (domainType) {
            case ACTIVITY -> activityPhotoRepository.findAllByActivityId(domainId)
                    .stream()
                    .map(ActivityPhoto::getPhoto)
                    .toList();
            case PRODUCT -> productPhotoRepository.findAllByProductId(domainId)
                    .stream()
                    .map(ProductPhoto::getPhoto)
                    .toList();
            default -> throw new GlobalException(PhotoErrorCode.DOMAIN_NOT_FOUND);
        };

        return photos.stream().map(PhotoCreateResponseDto::toDto).toList();
    }


    /**
     * 사진 수정
     *
     * @param domainId 상품Id 또는 활동Id
     * @param photoId 사진Id
     * @param photoCreateRequestDto {@link PhotoCreateRequestDto}
     * @param multipartFile {@link MultipartFile}
     * @param domainType {@link PhotoDomain}
     * @return {@link PhotoCreateResponseDto} 수정된 사진 응답값
     */
    @Override
    @Transactional
    public PhotoCreateResponseDto updatePhoto(Long domainId, Long photoId, PhotoCreateRequestDto photoCreateRequestDto, MultipartFile multipartFile, PhotoDomain domainType) {

        // 사진 유효성 검사
        validateMultipartFile(multipartFile);

        Photo photo = findByPhotoId(photoId);

        // 도메인 관계 검증
        validateRelation(domainId, photoId, domainType);

        photo.update(photoCreateRequestDto.getDescription(), multipartFile.getOriginalFilename());
        Photo updatedPhoto = photoRepository.save(photo);
        return PhotoCreateResponseDto.toDto(updatedPhoto);
    }

    /**
     * 사진 삭제
     *
     * @param domainId 상품Id 또는 활동Id
     * @param photoId 사진Id
     * @param domainType {@link PhotoDomain}
     */
    @Override
    @Transactional
    public void deletePhoto(Long domainId, Long photoId, PhotoDomain domainType) {

        Photo photo = findByPhotoId(photoId);

        // 도메인 관계 검증
        validateRelation(domainId, photoId, domainType);

        // 도메인에 따라 관계 엔티티 삭제
        switch (domainType) {
            case ACTIVITY -> {
                ActivityPhoto activityPhoto = activityPhotoRepository.findByActivityIdAndPhotoId(domainId, photoId)
                        .orElseThrow(() -> new GlobalException(PhotoErrorCode.RELATION_INVALID));
                activityPhotoRepository.delete(activityPhoto);
            }
            case PRODUCT -> {
                ProductPhoto productPhoto = productPhotoRepository.findByProductIdAndPhotoId(domainId, photoId)
                        .orElseThrow(() -> new GlobalException(PhotoErrorCode.RELATION_INVALID));
                productPhotoRepository.delete(productPhoto);
            }
            default -> throw new GlobalException(PhotoErrorCode.DOMAIN_NOT_FOUND);
        }

        // 사진 엔티티 삭제
        photoRepository.delete(photo);

    }

    /**
     * 사진Id에 해당하는 사진 반환
     *
     * @param photoId 사진Id
     * @return Photo {@link Photo}
     */
    private Photo findByPhotoId(Long photoId) {

        return photoRepository.findById(photoId)
                .orElseThrow(() -> new GlobalException(PhotoErrorCode.PHOTO_NOT_FOUND));
    }

    /**
     * 도메인 관계 검증
     *
     * @param domainId 상품Id 또는 활동Id
     * @param photoId 사진Id
     * @param domainType {@link PhotoDomain}
     */
    private void validateRelation(Long domainId, Long photoId, PhotoDomain domainType) {

        boolean isRelated = switch (domainType) {
            case ACTIVITY -> activityPhotoRepository.findByActivityIdAndPhotoId(domainId, photoId).isPresent();
            case PRODUCT -> productPhotoRepository.findByProductIdAndPhotoId(domainId, photoId).isPresent();
            default -> throw new GlobalException(PhotoErrorCode.DOMAIN_NOT_FOUND);
        };

        if (!isRelated) {
            throw new GlobalException(PhotoErrorCode.RELATION_INVALID);
        }
    }
}
