package com.shineidle.tripf.like.controller;

import io.swagger.v3.oas.annotations.Operation;
import com.shineidle.tripf.like.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feeds/{feedId}/likes")
public class LikeController {
    private final LikeService likeService;

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
        likeService.createLike(feedId);
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
        likeService.deleteLike(feedId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}