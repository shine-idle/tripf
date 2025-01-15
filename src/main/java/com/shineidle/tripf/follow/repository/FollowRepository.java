package com.shineidle.tripf.follow.repository;

import com.shineidle.tripf.follow.entity.Follow;
import com.shineidle.tripf.follow.entity.FollowPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, FollowPk> {
    //팔로워 조회
    List<Follow> findByFollowerId(Long followerId);

    //팔로잉 조회
    List<Follow> findByFollowingId(Long followingId);

    //팔로우 취소
    void deleteById(FollowPk followPk);

    //이미 팔로우 했는지 확인
    boolean existsById(FollowPk followPk);
}
