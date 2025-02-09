package com.shineidle.tripf.domain.follow.repository;

import com.shineidle.tripf.domain.follow.entity.Follow;
import com.shineidle.tripf.domain.follow.entity.FollowPk;
import com.shineidle.tripf.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, FollowPk> {
    /**
     * 나를 팔로우 한 팔로워 조회
     *
     * @param followerId 팔로워
     * @return 팔로워 {@Link Follow} 목록
     */
    List<Follow> findByFollowerId(User followerId);

    /**
     * 나를 팔로우한 팔로잉 조회
     *
     * @param followingId 팔로잉
     * @return 팔로잉 {@Link Follow} 목록
     */
    List<Follow> findByFollowingId(User followingId);

    /**
     * 팔로우 취소
     *
     * @param followPk
     */
    void deleteById(FollowPk followPk);

    /**
     * 팔로우 관계가 존재하는지 여부 확인
     *
     * @param followPk 팔로우 관계를 나타내는 객체
     * @return 팔로우 관계가 존재하면 {@code true}, 없을 경우 {@code false}
     */
    boolean existsById(FollowPk followPk);
}
