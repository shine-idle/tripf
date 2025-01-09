package com.shineidle.tripf.feed.controller;

import com.shineidle.tripf.feed.dto.FeedRequestDto;
import com.shineidle.tripf.feed.dto.FeedResponseDto;
import com.shineidle.tripf.feed.service.FeedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feeds")
public class FeedController {

    private final FeedService feedService;

    /**
     * 피드 작성
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
    public ResponseEntity<String> deleteFeed(
            @PathVariable Long feedId
    ) {
        String message = feedService.deleteFeed(feedId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * 활동 삭제
     */
    @DeleteMapping("/{feedId}/days/{daysId}/ativity/{activityId}")
    public ResponseEntity<String> deleteActivity(
            @PathVariable Long feedId,
            @PathVariable Long daysId,
            @PathVariable Long activityId
    ) {
        String message = feedService.deleteActivity(feedId, daysId, activityId);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    /**
     * 국가별 피드 조회
     */

    /**
     * 홈페이지
     */
}
