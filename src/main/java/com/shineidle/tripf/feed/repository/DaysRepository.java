package com.shineidle.tripf.feed.repository;

import com.shineidle.tripf.feed.entity.Days;
import com.shineidle.tripf.feed.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DaysRepository extends JpaRepository<Days, Long> {

    List<Days> findByFeed(Feed feed);
}
