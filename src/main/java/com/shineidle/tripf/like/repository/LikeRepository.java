package com.shineidle.tripf.like.repository;

import com.shineidle.tripf.like.dto.FeedLikeDto;
import com.shineidle.tripf.like.entity.Like;
import com.shineidle.tripf.like.entity.LikePk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, LikePk> {

    /**
     * 이미 좋아요 했는지 확인
     */
    boolean existsById(LikePk likePk);

    /**
     * 좋아요 취소
     */
    void deleteById(LikePk likePk);

    // 좋아요 상위 5개
    @Query("SELECT new com.shineidle.tripf.like.dto.FeedLikeDto(" +
            "l.feed.id, f.title, COUNT(l)) " +
            "FROM Like l " +
            "JOIN l.feed f " +
            "GROUP BY l.feed.id, f.title " +
            "ORDER BY COUNT(l) DESC")
    List<FeedLikeDto> findTop5FeedsWithLikeCount(Pageable pageable);
}