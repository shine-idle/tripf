package com.shineidle.tripf.like.service;

import com.shineidle.tripf.like.dto.FeedLikeDto;

import java.util.List;

public interface LikeService {
   //좋아요
    void createLike(Long feedId);

    //좋아요 취소
    void deleteLike(Long feedId);

    //좋아요 상위 5개 피드
    public List<FeedLikeDto> getTop5LikedFeeds();
}