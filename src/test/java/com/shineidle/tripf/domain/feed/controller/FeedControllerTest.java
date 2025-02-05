package com.shineidle.tripf.domain.feed.controller;

import com.shineidle.tripf.domain.feed.dto.FeedRequestDto;
import com.shineidle.tripf.domain.feed.dto.FeedResponseDto;
import com.shineidle.tripf.domain.feed.service.FeedService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class FeedControllerTest {
    @InjectMocks
    private FeedController feedController;

    @Mock
    private FeedService feedService;

    @Test
    void createFeed() {
        // given
        FeedRequestDto requestDto = new FeedRequestDto(
                "Seoul", LocalDateTime.now(), LocalDateTime.now().plusDays(5),
                "여행 제목", "여행 내용", 500000L, "#여행", Collections.emptyList());

        FeedResponseDto responseDto = new FeedResponseDto(
                1L, null, "사용자 이름", "Seoul",
                LocalDateTime.now(), LocalDateTime.now().plusDays(5),
                "여행 제목", "여행 내용", 500000L, "#여행",
                LocalDateTime.now(), LocalDateTime.now(), Collections.emptyList());

        Mockito.when(feedService.createFeed(any(FeedRequestDto.class)))
                .thenReturn(responseDto);

        // when
        ResponseEntity<FeedResponseDto> response = feedController.createFeed(requestDto);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getId()).isEqualTo(1L);
        assertThat(response.getBody().getCity()).isEqualTo("Seoul");
    }
}
