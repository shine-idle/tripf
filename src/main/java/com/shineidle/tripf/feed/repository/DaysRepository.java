package com.shineidle.tripf.feed.repository;

import com.shineidle.tripf.feed.entity.Days;
import com.shineidle.tripf.feed.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DaysRepository extends JpaRepository<Days, Long> {

    /**
     * 피드에 속한 일정을 List로 반환
     *
     * @param feed 피드 식별자
     * @return findByFeed
     */
    List<Days> findByFeed(Feed feed);

    /**
     * 일정이 피드에 속하는지 확인
     *
     * @param daysId 일정 식별자
     * @param feedId 피드 식별자
     * @return findByIdWithFeed
     */
    @Query("SELECT d FROM Days d JOIN FETCH d.feed f WHERE d.id = :daysId AND f.id = :feedId")
    Optional<Days> findByIdWithFeed(
            @Param("daysId") Long daysId,
            @Param("feedId") Long feedId
    );

    /**
     * 일정이 피드에 있는지 확인
     *
     * @param feed 피드
     * @param date 일정
     * @return existsByFeedAndDate
     */
    boolean existsByFeedAndDate(Feed feed, LocalDate date);
}
