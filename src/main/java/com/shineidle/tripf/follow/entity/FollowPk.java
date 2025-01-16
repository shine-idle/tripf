package com.shineidle.tripf.follow.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class FollowPk implements Serializable {
    private Long followerId;
    private Long followingId;

    public FollowPk(Long followerId, Long followingId) {
        this.followerId = followerId;
        this.followingId = followingId;
    }

    public static FollowPk of(Long followerId, Long followingId) {
        return new FollowPk(followerId, followingId);
    }
}
