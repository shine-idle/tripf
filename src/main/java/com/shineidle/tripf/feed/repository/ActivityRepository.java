package com.shineidle.tripf.feed.repository;

import com.shineidle.tripf.feed.entity.Activity;
import com.shineidle.tripf.feed.entity.Days;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    /**
     * 일정에 속한 활동을 List로 반환
     *
     * @param days 일정 식별자
     * @return findByDays
     */
    List<Activity> findByDays(Days days);

    /**
     * 활동이 일정과 피드에 속하는지 확인
     *
     * @param activityId 활동 식별자
     * @param daysId 일정 식별자
     * @param feedId 피드 식별자
     * @return findByIdWithFeedAndDays
     */
    @Query("SELECT a FROM Activity a " +
            "JOIN a.days d " +
            "JOIN d.feed f " +
            "WHERE a.id = :activityId AND d.id = :daysId AND f.id = :feedId")
    Optional<Activity> findByIdWithFeedAndDays(
            @Param("activityId") Long activityId,
            @Param("daysId") Long daysId,
            @Param("feedId") Long feedId
    );

}