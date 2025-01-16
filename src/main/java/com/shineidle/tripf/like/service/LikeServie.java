package com.shineidle.tripf.like.service;

public interface LikeServie {

    /**
     * 좋아요
     */
    void createLike(Long feedId);

    /**
     * 좋아요 취소
     */
    void deleteLike(Long feedId);
}