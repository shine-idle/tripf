package com.shineidle.tripf.domain.feed.service;

import com.shineidle.tripf.global.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.domain.feed.dto.*;
import com.shineidle.tripf.domain.feed.entity.Feed;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FeedService {
    /**
     * 피드 작성
     */
    FeedResponseDto createFeed(@Valid FeedRequestDto feedRequestDto, String token);
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
     * 활동 수정
     */
    FeedResponseDto updateActivity(Long feedId, Long daysId, Long activityId, @Valid ActivityRequestDto activityRequestDto);

    /**
     * 국가별 피드 조회
     */
    List<RegionResponseDto> findRegion(String country);

    /**
     *피드 ID로 피드 확인
     */
    Feed checkFeed(Long feedId);

    /**
     * 홈페이지
     */
    HomeResponseDto findHomeData();

    /**
     * 본인 피드 조회
     */
    Page<MyFeedResponseDto> findMyFeeds(Pageable pageable);

    HomeResponseDto findPublicHomeData();
}