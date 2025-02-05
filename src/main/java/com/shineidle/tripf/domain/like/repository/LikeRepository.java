package com.shineidle.tripf.domain.like.repository;

import com.shineidle.tripf.domain.like.dto.FeedLikeDto;
import com.shineidle.tripf.domain.like.entity.Like;
import com.shineidle.tripf.domain.like.entity.LikePk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, LikePk> {
    /**
     * 사용자가 피드에 좋아요를 눌렀는지 확인
     *
     * @param likePk {@link LikePk} 객체로, 팔로우 관계를 나타내는 복합 키
     * @return 좋아요가 눌렸으면 {@code true}, 누르지 않았다면 {@code false}
     */
    boolean existsById(LikePk likePk);

    /**
     * 좋아요 취소
     *
     * @param likePk {@link LikePk} 객체로, 좋아요를 취소할 피드와 사용자 식별
     */
    void deleteById(LikePk likePk);

    /**
     * 좋아요 상위 5개 피드 조회
     *
     * @param pageable 페이징 정보, 상위 5개의 피드 가져 옴
     * @return {@Link FeedLikeDto}
     */
    @Query("SELECT new com.shineidle.tripf.domain.like.dto.FeedLikeDto(" +
            "l.feed.id, f.title, COUNT(l)) " +
            "FROM Like l " +
            "JOIN l.feed f " +
            "GROUP BY l.feed.id, f.title " +
            "ORDER BY COUNT(l) DESC")
    List<FeedLikeDto> findTop5FeedsWithLikeCount(Pageable pageable);
}