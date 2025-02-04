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
import java.util.List;

@RestController
@RequestMapping("/api/activities/{activityId}/photos")
@RequiredArgsConstructor
public class ActivityPhotoController {
    private final PhotoService photoService;

    /**
     * 활동 사진 업로드
     *
     * @param activityId 활동 식별자
     * @param photoRequestDto {@link PhotoRequestDto} 사진 요청 Dto
     * @param file {@link MultipartFile} 첨부할 사진
     * @return {@link PhotoResponseDto} 사진 응답 Dto
     */
    @Operation(summary = "활동 사진 업로드")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<PhotoResponseDto> uploadPhotoToActivity(
            @PathVariable Long activityId,
            @ModelAttribute PhotoRequestDto photoRequestDto,
            @RequestParam(value = "file",required = false) MultipartFile file
    ) throws IOException {
        PhotoResponseDto responseDto = photoService.uploadPhoto(activityId, photoRequestDto, file, PhotoDomain.ACTIVITY);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    /**
     * 활동 사진 단건 조회
     *
     * @param activityId 활동 식별자
     * @param photoId 사진 식별자
     * @return {@link PhotoResponseDto} 사진 응답 Dto
     */
    @Operation(summary = "활동 사진 단건 조회")
    @GetMapping("/{photoId}")
    public ResponseEntity<PhotoResponseDto> findActivityPhoto(
            @PathVariable Long activityId,
            @PathVariable Long photoId
    ) {
        PhotoResponseDto responseDto = photoService.findPhoto(activityId, photoId, PhotoDomain.ACTIVITY);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * 활동 사진 다건 조회
     *
     * @param activityId 활동 식별
     * @return {@link PhotoResponseDto} 사진 응답 Dto
     */
    @Operation(summary = "활동 사진 다건 조회")
    @GetMapping()
    public ResponseEntity<List<PhotoResponseDto>> findAllActivityPhoto(
            @PathVariable Long activityId
    ) {
        List<PhotoResponseDto> responseDtos = photoService.findAllPhoto(activityId, PhotoDomain.ACTIVITY);

        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }


    /**
     * 활동 사진 수정
     *
     * @param activityId 활동 식별자
     * @param photoId 사진 식별자
     * @param photoRequestDto {@link PhotoRequestDto}
     * @param file {@link MultipartFile} 첨부할 사진
     * @return {@link PhotoResponseDto} 사진 응답 Dto
     */
    @Operation(summary = "활동 사진 수정")
    @RequestMapping(
            value = "/{photoId}",
            method = RequestMethod.PATCH,
            consumes = "multipart/form-data"
    )
    public ResponseEntity<PhotoResponseDto> updateActivityPhoto(
            @PathVariable Long activityId,
            @PathVariable Long photoId,
            @ModelAttribute PhotoRequestDto photoRequestDto,
            @RequestParam MultipartFile file
    ) {
        PhotoResponseDto responseDto = photoService.updatePhoto(activityId, photoId, photoRequestDto, file, PhotoDomain.ACTIVITY);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    /**
     * 활동 사진 삭제
     *
     * @param activityId 활동 식별자
     * @param photoId 사진 식별자
     * @return {@link PostMessageResponseDto} 사진 삭제 문구
     */
    @Operation(summary = "활동 사진 삭제")
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