package com.shineidle.tripf.domain.feed.controller;

import com.shineidle.tripf.domain.feed.dto.HomeResponseDto;
import com.shineidle.tripf.domain.feed.service.FeedService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class HomeController {
    private final FeedService feedService;

    /**
     * 홈페이지 데이터를 반환합니다
     *
     * @return {@link HomeResponseDto}
     */
    @Operation(summary = "홈페이지")
    @GetMapping
    public ResponseEntity<HomeResponseDto> home() {
        HomeResponseDto homeResponseDto = feedService.findHomeData();
        return new ResponseEntity<>(homeResponseDto, HttpStatus.OK);
    }
}
