package com.shineidle.tripf.follow.entity;

import com.shineidle.tripf.common.BaseEntity;
import com.shineidle.tripf.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@IdClass(FollowPk.class)
@Table(name = "`follow`")
public class Follow extends BaseEntity {
    /**
     * 팔로워 ID(나를 팔로우한 사람의 ID)
     */
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private User followerId;

    /**
     * 팔로잉 ID(내가 팔로우한 사람의 ID)
     */
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id")
    private User followingId;

    public Follow() {}

    public Follow(User followerId, User followingId) {
        this.followerId = followerId;
        this.followingId = followingId;
    }
}
