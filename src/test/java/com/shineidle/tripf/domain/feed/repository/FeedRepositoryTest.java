package com.shineidle.tripf.domain.feed.repository;

import com.shineidle.tripf.domain.feed.entity.Feed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class FeedRepositoryTest {

    @Autowired
    private FeedRepository feedRepository;

    @Test
    @DisplayName("삭제되지 않은 특정 국가의 피드를 조회")
    void findByCountryAndDeletedAtIsNull() {
        // given
        String country = "Korea";

        // when
        List<Feed> feeds = feedRepository.findByCountryAndDeletedAtIsNull(country);

        // then
        assertThat(feeds).isNotNull();
    }
}