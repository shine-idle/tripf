package com.shineidle.tripf.domain.comment.entity;

import com.shineidle.tripf.global.common.entity.BaseEntity;
import com.shineidle.tripf.domain.feed.entity.Feed;
import com.shineidle.tripf.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Table(name = "`comment`")
@DynamicUpdate
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id", nullable = false)
    private Feed feed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String comment;

    /**
     * 생성자 - 약속된 형태로만 생성가능하도록 합니다.
     */
    public Comment() {}

    public Comment(Feed feed, User user, String comment) {
        this.feed = feed;
        this.user = user;
        this.comment = comment;
    }

    /**
     * 서비스 메소드 - 외부에서 엔티티를 수정할 메소드를 정의합니다. (단일 책임을 가지도록 주의합니다.)
     */
    public void update(String comment) {
        this.comment = comment;
    }
}
