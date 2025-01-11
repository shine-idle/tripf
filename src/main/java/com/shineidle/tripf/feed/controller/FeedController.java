package com.shineidle.tripf.feed.controller;

import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.feed.dto.*;
import com.shineidle.tripf.feed.service.FeedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feeds")
public class FeedController {

    private final FeedService feedService;

    /**
     * 피드 작성
     * 피드 작성 시 피드, 일정, 활동 모두 작성
     */
    @PostMapping
    public ResponseEntity<FeedResponseDto> createFeed(
            @Valid @RequestBody FeedRequestDto feedRequestDto
    ) {
        FeedResponseDto feedResponseDto = feedService.createFeed(feedRequestDto);
        return new ResponseEntity<>(feedResponseDto, HttpStatus.CREATED);
    }

    /**
     * 피드 수정
     */
    @PutMapping("/{feedId}")
    public ResponseEntity<FeedResponseDto> updateFeed(
            @PathVariable Long feedId,
            @Valid @RequestBody FeedRequestDto feedRequestDto
    ) {
        FeedResponseDto feedResponseDto = feedService.updateFeed(feedId, feedRequestDto);
        return new ResponseEntity<>(feedResponseDto, HttpStatus.OK);
    }

    /**
    * 피드 상세 조회
    */
    @GetMapping("/{feedId}")
    public ResponseEntity<FeedResponseDto> findFeed(
            @PathVariable Long feedId
    ) {
        FeedResponseDto feedResponseDto = feedService.findFeed(feedId);
        return new ResponseEntity<>(feedResponseDto, HttpStatus.OK);
    }

    /**
     * 피드 삭제
     */
    @DeleteMapping("/{feedId}")
    public ResponseEntity<PostMessageResponseDto> deleteFeed(
            @PathVariable Long feedId
    ) {
        PostMessageResponseDto postMessageResponseDto = feedService.deleteFeed(feedId);
        return new ResponseEntity<>(postMessageResponseDto, HttpStatus.OK);
    }

    /**
     * 일정 삭제
     */
    @DeleteMapping("/{feedId}/days/{daysId}")
    public ResponseEntity<PostMessageResponseDto> deleteDays(
            @PathVariable Long feedId,
            @PathVariable Long daysId
    ) {
        PostMessageResponseDto postMessageResponseDto = feedService.deleteDays(feedId, daysId);
        return new ResponseEntity<>(postMessageResponseDto, HttpStatus.OK);
    }

    /**
     * 활동 삭제
     */
    @DeleteMapping("/{feedId}/days/{daysId}/activities/{activityId}")
    public ResponseEntity<PostMessageResponseDto> deleteActivity(
            @PathVariable Long feedId,
            @PathVariable Long daysId,
            @PathVariable Long activityId
    ) {
        PostMessageResponseDto postMessageResponseDto = feedService.deleteActivity(feedId, daysId, activityId);
        return new ResponseEntity<>(postMessageResponseDto, HttpStatus.OK);
    }

    /**
     * 일정 추가
     * 일정 추가 시 활동도 같이 추가
     */
    @PostMapping("/{feedId}/days")
    public ResponseEntity<FeedResponseDto> createDay(
            @PathVariable Long feedId,
            @Valid @RequestBody DaysRequestDto daysRequestDto
    ) {
        FeedResponseDto feedResponseDto = feedService.createDay(feedId,daysRequestDto);
        return new ResponseEntity<>(feedResponseDto, HttpStatus.OK);
    }

    /**
     * 활동 추가
     */
    @PostMapping("/{feedId}/days/{daysId}/activities")
    public ResponseEntity<FeedResponseDto> createActivity(
            @PathVariable Long feedId,
            @PathVariable Long daysId,
            @Valid @RequestBody ActivityRequestDto activityRequestDto
    ) {
        FeedResponseDto feedResponseDto = feedService.createActivity(feedId, daysId, activityRequestDto);
        return new ResponseEntity<>(feedResponseDto, HttpStatus.OK);
    }

    /**
     * 활동 수정
     */
    @PutMapping("/{feedId}/days/{daysId}/activities/{activityId}")
    public ResponseEntity<FeedResponseDto> updateActivity(
            @PathVariable Long feedId,
            @PathVariable Long daysId,
            @PathVariable Long activityId,
            @Valid @RequestBody ActivityRequestDto activityRequestDto
    ) {
        FeedResponseDto feedResponseDto = feedService.updateActivity(feedId, daysId, activityId ,activityRequestDto);
        return new ResponseEntity<>(feedResponseDto, HttpStatus.OK);
    }

    /**
     * 국가별 피드 조회
     */
    @GetMapping
    public ResponseEntity<List<RegionResponseDto>> findRegion(
            @RequestParam(value = "region", required = false) String country
    ) {
        List<RegionResponseDto> regionResponseDto = feedService.findRegion(country);
        return new ResponseEntity<>(regionResponseDto, HttpStatus.OK);
    }
}
