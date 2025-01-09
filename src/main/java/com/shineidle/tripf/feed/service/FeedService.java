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
    FeedResponseDto updateFeed(@Valid FeedRequestDto feedRequestDto);

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
