package com.shineidle.tripf.follow.controller;

import com.shineidle.tripf.follow.dto.FollowResponseDto;
import com.shineidle.tripf.follow.service.FollowService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/follows")
public class FollowController {
    private final FollowService followService;

    /**
     * 팔로우 추가
     *
     * @param followingId 팔로잉 식별자
     * @return 팔로잉 되었습니다 문구
     */
    @Operation(summary = "팔로우 추가")
    @PostMapping("/{followingId}")
    public ResponseEntity<String> createFollow(
            @PathVariable Long followingId
    ) {
        try {
            followService.createFollow(followingId);
            return new ResponseEntity<>("팔로잉 되었습니다", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 팔로워(나를 팔로우 한 사람들) 조회
     *
     * @return {@link FollowResponseDto} 팔로우 응답 Dto
     */
    @Operation(summary = "팔로워(나를 팔로우 한 사람들) 조회")
    @GetMapping("/followers")
    public ResponseEntity<List<FollowResponseDto>> findFollowers() {
        try {
            List<FollowResponseDto> followers = followService.findFollowers();
            return new ResponseEntity<>(followers, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 팔로잉(내가 팔로우 한 사람들) 조회
     *
     * @return {@link FollowResponseDto} 팔로우 응답 Dto
     */
    @Operation(summary = "팔로잉(내가 팔로우 한 사람들) 조회")
    @GetMapping("/followings")
    public ResponseEntity<List<FollowResponseDto>> findFollowings() {
        try {
            List<FollowResponseDto> followings = followService.findFollowings();
            return new ResponseEntity<>(followings, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 언팔로잉(내가 팔로우한 사람들 삭제)
     *
     * @param followingId 팔로잉 식별자
     * @return 팔로우가 취소되었습니다 문구
     */
    @Operation(summary = "언팔로잉(내가 팔로우한 사람들 삭제)")
    @DeleteMapping("/unfollow/following/{followingId}")
    public ResponseEntity<String> deleteFollowByFollowingId(
            @PathVariable Long followingId
    ) {
        try {
            followService.deleteFollowByFollowingId(followingId);
            return new ResponseEntity<>("팔로우가 취소 되었습니다", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 언팔로우(나를 팔로우한 사람들 삭제)
     *
     * @param followerId 팔로워 식별자
     * @return 팔로우가 취소되었습니다 문구
     */
    @Operation(summary = "언팔로우(나를 팔로우한 사람들 삭제)")
    @DeleteMapping("/unfollow/follower/{followerId}")
    public ResponseEntity<String> deleteFollowByFollowerId(
            @PathVariable Long followerId
    ) {
        try {
            // 나를 팔로우한 사람을 언팔로우
            followService.deleteFollowByFollowerId(followerId);
            return new ResponseEntity<>("팔로우가 취소 되었습니다", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
