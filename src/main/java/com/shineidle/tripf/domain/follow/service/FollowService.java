package com.shineidle.tripf.domain.follow.service;

import com.shineidle.tripf.domain.follow.dto.FollowResponseDto;

import java.util.List;

public interface FollowService {
    /**
     * 팔로우 추가
     *
     * @param followingId 내가 팔로우 할 사용자의 ID
     */
    void createFollow(Long followingId);

    /**
     * 팔로워 조회
     *
     * @return {@link List<FollowResponseDto>}
     */
    List<FollowResponseDto> findFollowers();

    /**
     * 팔로잉 조회
     *
     * @return {@link List<FollowResponseDto>}
     */
    List<FollowResponseDto> findFollowings();

    /**
     * 필로잉 삭제
     *
     * @param followingId 내가 팔로우한 사용자의 ID
     */
    void deleteFollowByFollowingId(Long followingId);

    /**
     * 팔로워 삭제
     *
     * @param followerId 나를 팔로우한 사용자의 ID
     */
    void deleteFollowByFollowerId(Long followerId);
}