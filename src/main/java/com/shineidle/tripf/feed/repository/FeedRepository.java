package com.shineidle.tripf.feed.repository;

import com.shineidle.tripf.feed.entity.Feed;
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
}