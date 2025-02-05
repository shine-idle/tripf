package com.shineidle.tripf.domain.like.entity;

import com.shineidle.tripf.global.common.entity.BaseEntity;
import com.shineidle.tripf.domain.feed.entity.Feed;
import com.shineidle.tripf.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@IdClass(LikePk.class)
@Table(name = "`like`")
public class Like extends BaseEntity {
    @Id
    @ManyToOne(targetEntity = Feed.class ,fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @Id
    @ManyToOne(targetEntity = User.class ,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    public Like() {}

    public Like(Feed feed, User user) {
        this.feed = feed;
        this.user = user;
    }
}