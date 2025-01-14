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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/activities/{activityId}/photos")
@RequiredArgsConstructor
public class ActivityPhotoController {

    private final PhotoService photoService;

    /**
     * 활동 사진 업로드
     *
     * @param activityId 활동Id
     * @param photoCreateRequestDto {@link PhotoCreateRequestDto}
     * @param file {@link MultipartFile}
     * @return {@link PhotoCreateResponseDto} 생성된 사진 응답값
     */
    @PostMapping
    public ResponseEntity<PhotoCreateResponseDto> uploadPhotoToActivity(
            @PathVariable Long activityId,
            @ModelAttribute PhotoCreateRequestDto photoCreateRequestDto,
            @RequestParam MultipartFile file
    ) throws IOException {
        PhotoCreateResponseDto responseDto = photoService.uploadPhoto(activityId, photoCreateRequestDto, file, PhotoDomain.ACTIVITY);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * 활동 사진 단건 조회
     *
     * @param activityId 활동Id
     * @param photoId 사진Id
     * @return {@link PhotoCreateResponseDto} 조회된 사진 응답값
     */
    @GetMapping("/{photoId}")
    public ResponseEntity<PhotoCreateResponseDto> findActivityPhoto(
            @PathVariable Long activityId,
            @PathVariable Long photoId
    ) {
        PhotoCreateResponseDto responseDto = photoService.findPhoto(activityId, photoId, PhotoDomain.ACTIVITY);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * 활동 사진 다건 조회
     *
     * @param activityId 활동Id
     * @return {@link PhotoCreateResponseDto} 조회된 사진 응답값
     */
    @GetMapping()
    public ResponseEntity<List<PhotoCreateResponseDto>> findAllActivityPhoto(
            @PathVariable Long activityId
    ) {
        List<PhotoCreateResponseDto> responseDtos = photoService.findAllPhoto(activityId, PhotoDomain.ACTIVITY);

        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }


    /**
     * 활동 사진 수정
     *
     * @param activityId 활동Id
     * @param photoId 사진Id
     * @param photoCreateRequestDto {@link PhotoCreateRequestDto}
     * @param file {@link MultipartFile}
     * @return {@link PhotoCreateResponseDto} 수정된 사진 응답값
     */
    @PatchMapping("/{photoId}")
    public ResponseEntity<PhotoCreateResponseDto> updateActivityPhoto(
            @PathVariable Long activityId,
            @PathVariable Long photoId,
            @ModelAttribute PhotoCreateRequestDto photoCreateRequestDto,
            @RequestParam MultipartFile file
    ) {
        PhotoCreateResponseDto responseDto = photoService.updatePhoto(activityId, photoId, photoCreateRequestDto, file, PhotoDomain.ACTIVITY);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * 활동 사진 삭제
     *
     * @param activityId 활동Id
     * @param photoId 사진Id
     * @return {@link PostMessageResponseDto} 사진 삭제 문구
     */
    @DeleteMapping("/{photoId}")
    public ResponseEntity<PostMessageResponseDto> deleteActivityPhoto(
            @PathVariable Long activityId,
            @PathVariable Long photoId
    ) {
        photoService.deletePhoto(activityId, photoId, PhotoDomain.ACTIVITY);

        PostMessageResponseDto responseDto = new PostMessageResponseDto(PostMessage.PHOTO_DELETED);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
