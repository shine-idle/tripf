package com.shineidle.tripf.feed.repository;

import com.shineidle.tripf.feed.entity.Days;
import com.shineidle.tripf.feed.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DaysRepository extends JpaRepository<Days, Long> {

    List<Days> findByFeed(Feed feed);
}
