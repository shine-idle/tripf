package com.shineidle.tripf.domain.like.service;

import com.shineidle.tripf.domain.like.dto.FeedLikeDto;

import java.util.List;

public interface LikeService {
    /**
     * 좋아요 추가
     *
     * @param feedId 좋아요를 추가 할 피드의 ID
     */
    void createLike(Long feedId);

    /**
     * 좋아요 취소
     *
     * @param feedId 좋아요를 추가 할 피드의 ID
     */
    void deleteLike(Long feedId);

    /**
     * 좋아요 상위 5개 피드
     *
     * @return {@link List<FeedLikeDto>}상위 5개의 좋아요를 받은 피드 리스트
     */
    public List<FeedLikeDto> getTop5LikedFeedsWithImages();
}