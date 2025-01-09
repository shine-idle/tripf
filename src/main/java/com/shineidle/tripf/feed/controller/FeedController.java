package com.shineidle.tripf.feed.controller;

import com.shineidle.tripf.feed.dto.FeedRequestDto;
import com.shineidle.tripf.feed.dto.FeedResponseDto;
import com.shineidle.tripf.feed.service.FeedService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /**
    * 피드 상세 조회
    */

    /**
     * 피드 삭제
     */

    /**
     * 활동 삭제
     */

    /**
     * 국가별 피드 조회
     */

    /**
     * 홈페이지
     */
}
