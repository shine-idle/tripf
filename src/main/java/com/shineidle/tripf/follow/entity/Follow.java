package com.shineidle.tripf.follow.entity;

import com.shineidle.tripf.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@IdClass(FollowPk.class)
@Table(name = "`follow`")
public class Follow extends BaseEntity {
    //팔로워 ID(나를 팔로우한 사람의 ID)
    @Id
    private Long followerId;

    //팔로잉 ID(내가 팔로우한 사람의 ID)
    @Id
    private Long followingId;

    public Follow() {}

    public Follow(Long followerId, Long followingId) {
        this.followerId = followerId;
        this.followingId = followingId;
    }
}
