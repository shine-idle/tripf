package com.shineidle.tripf.domain.feed.repository;

import com.shineidle.tripf.domain.feed.entity.Feed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {

    List<Feed> findByCountryAndDeletedAtIsNull(String country);

    List<Feed> findByCountry(String country);

    List<Feed> findByCountryNot(String korea);

    @Query("SELECT DISTINCT f.country FROM Feed f")
    List<String> findDistinctCountries();

    Page<Feed> findByUserIdAndDeletedAtIsNull(Long userId, Pageable pageable);
}