package com.shineidle.tripf.like.repository;

import com.shineidle.tripf.like.entity.Like;
import com.shineidle.tripf.like.entity.LikePk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, LikePk> {

    /**
     * 이미 좋아요 했는지 확인
     */
    boolean existsById(LikePk likePk);

    /**
     * 좋아요 취소
     */
    void deleteById(LikePk likePk);
}