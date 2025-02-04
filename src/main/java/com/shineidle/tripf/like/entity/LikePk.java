package com.shineidle.tripf.like.entity;

import com.shineidle.tripf.feed.entity.Feed;
import com.shineidle.tripf.user.entity.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class LikePk implements Serializable {
    private Feed feed;
    private User user;

    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    public LikePk(Feed feed, User user) {
        this.feed = feed;
        this.user = user;
    }
}