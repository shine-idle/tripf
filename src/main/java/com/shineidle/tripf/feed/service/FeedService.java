package com.shineidle.tripf.feed.service;

import com.shineidle.tripf.feed.dto.FeedRequestDto;
import com.shineidle.tripf.feed.dto.FeedResponseDto;
import jakarta.validation.Valid;

public interface FeedService {

    /**
     * 피드 작성
     */
    FeedResponseDto createFeed(@Valid FeedRequestDto feedRequestDto);

    /**
     * 피드 수정
     */
    FeedResponseDto updateFeed(Long feedId, @Valid FeedRequestDto feedRequestDto);

    /**
     * 피드 상세 조회
     */
    FeedResponseDto findFeed(Long feedId);

    /**
     * 피드 삭제
     */
    String deleteFeed(Long feedId);

    /**
     * 활동 삭제
     */
    String deleteActivity(Long feedId, Long daysId, Long activityId);

    /**
     * 국가별 피드 조회
     */

    /**
     * 홈페이지
     */
}
