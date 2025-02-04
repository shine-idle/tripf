package com.shineidle.tripf.follow.entity;

import com.shineidle.tripf.user.entity.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class FollowPk implements Serializable {
    private User followerId;
    private User followingId;

    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    public FollowPk(User followerId, User followingId) {
        this.followerId = followerId;
        this.followingId = followingId;
    }

    public static FollowPk of(User followerId, User followingId) {
        return new FollowPk(followerId, followingId);
    }
}