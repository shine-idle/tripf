package com.shineidle.tripf.follow.service;

import com.shineidle.tripf.follow.dto.FollowResponseDto;

import java.util.List;

public interface FollowService {
    /**
     * 팔로우 추가
     */
    void createFollow(Long followingId);

    /**
     * 팔로워 조회
     */
    List<FollowResponseDto> findFollowers();

    /**
     * 팔로잉 조회
     */
    List<FollowResponseDto> findFollowings();

    /**
     * 팔로잉 삭제
     */
    void deleteFollowByFollowingId(Long followingId);

    /**
     * 팔로워 삭제
     */
    void deleteFollowByFollowerId(Long followerId);
}
