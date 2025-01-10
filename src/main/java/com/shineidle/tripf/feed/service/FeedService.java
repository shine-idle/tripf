package com.shineidle.tripf.feed.service;

import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.feed.dto.*;
import jakarta.validation.Valid;

import java.util.List;

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
    PostMessageResponseDto deleteFeed(Long feedId);

    /**
     * 일정 삭제
     */
    PostMessageResponseDto deleteDays(Long feedId, Long daysId);

    /**
     * 활동 삭제
     */
    PostMessageResponseDto deleteActivity(Long feedId, Long daysId, Long activityId);

    /**
     * 일정 추가
     * 일정 추가 시 활동도 같이 추가
     */
    FeedResponseDto createDay(Long feedId, @Valid DaysRequestDto daysRequestDto);

    /**
     * 활동 추가
     */
    FeedResponseDto createActivity(Long feedId, Long daysId, @Valid ActivityRequestDto activityRequestDto);

    /**
     * 일정, 활동 수정
     */
    FeedResponseDto updateDay(Long feedId, @Valid DaysRequestDto daysRequestDto);

    /**
     * 국가별 피드 조회
     */
    List<RegionResponseDto> findRegion(String city);


}
