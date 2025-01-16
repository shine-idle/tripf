package com.shineidle.tripf.like.entity;

import com.shineidle.tripf.common.BaseEntity;
import com.shineidle.tripf.feed.entity.Feed;
import com.shineidle.tripf.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@IdClass(LikePk.class)
@Table(name = "`like`")
public class Like extends BaseEntity {
    @Id
    @ManyToOne(targetEntity = Feed.class ,fetch = FetchType.LAZY)
    @JoinColumn(name = "feedId")
    private Feed feed;

    @Id
    @ManyToOne(targetEntity = User.class ,fetch = FetchType.LAZY)
    @JoinColumn(name = "userID")
    private User user;

    public Like() {}

    public Like(Feed feed, User user) {
        this.feed = feed;
        this.user = user;
    }
}