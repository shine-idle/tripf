package com.shineidle.tripf.like.controller;

import com.shineidle.tripf.like.service.LikeServie;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feeds/{feedId}/likes")
public class LikeController {
    private final LikeServie likeServie;

    /**
     * 좋아요
     *
     * @param feedId 피드 식별자
     * @return HTTP 상태 코드 201 (CREATED)
     */
    @Operation(summary = "좋아요")
    @PostMapping
    public ResponseEntity<Void> createLike(
            @PathVariable Long feedId
    ) {
        likeServie.createLike(feedId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 좋아요 취소
     *
     * @param feedId
     * @return HTTP 상태 코드 200 (OK)
     */
    @Operation(summary = "좋아요 취소")
    @DeleteMapping
    public ResponseEntity<Void> deleteLike(
            @PathVariable Long feedId
    ) {
        likeServie.deleteLike(feedId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}