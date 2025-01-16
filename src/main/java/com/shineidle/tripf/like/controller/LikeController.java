package com.shineidle.tripf.like.controller;

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

    //좋아요
    @PostMapping
    public ResponseEntity<String> createLike(
            @PathVariable Long feedId
    ) {
        likeService.createLike(feedId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //좋아요 취소
    @DeleteMapping
    public ResponseEntity<String> deleteLike(
            @PathVariable Long feedId
    ) {
        likeService.deleteLike(feedId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}