package com.shineidle.tripf.feed.repository;

import com.shineidle.tripf.feed.entity.Days;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DaysRepository extends JpaRepository<Days, Long> {

    @Query("SELECT d FROM Days d JOIN FETCH d.feed f WHERE d.id = :daysId AND f.id = :feedId")
    Optional<Days> findByFeedIdAndDaysId(Long feedId, Long daysId);
}
