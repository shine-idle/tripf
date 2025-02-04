package com.shineidle.tripf.comment.controller;

import com.shineidle.tripf.comment.dto.CommentRequestDto;
import com.shineidle.tripf.comment.dto.CommentResponseDto;
import com.shineidle.tripf.comment.service.CommentService;
import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feeds/{feedId}/comments")
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 작성
     *
     * @param feedId            피드 식별자
     * @param commentRequestDto {@link CommentRequestDto} 댓글 요청 Dto
     * @return {@link CommentResponseDto} 댓글 응답 Dto
     */
    @Operation(summary = "댓글 작성")
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long feedId,
            @Valid @RequestBody CommentRequestDto commentRequestDto
    ) {
        CommentResponseDto commentResponseDto = commentService.createComment(feedId, commentRequestDto);
        return new ResponseEntity<>(commentResponseDto, HttpStatus.CREATED);
    }

    /**
     * 댓글 수정
     *
     * @param feedId            피드 식별자
     * @param commentId         댓글 식별자
     * @param commentRequestDto {@link CommentRequestDto} 댓글 요청 Dto
     * @return {@link CommentResponseDto} 댓글 응답 Dto
     */
    @Operation(summary = "댓글 수정")
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long feedId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequestDto commentRequestDto
    ) {
        CommentResponseDto commentResponseDto = commentService.updateComment(feedId, commentId, commentRequestDto);
        return new ResponseEntity<>(commentResponseDto, HttpStatus.OK);
    }

    /**
     * 댓글 조회
     *
     * @param feedId 피드 식별자
     * @return {@link CommentResponseDto} 댓글 응답 Dto
     */
    @Operation(summary = "댓글 조회")
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> findAllComment(
            @PathVariable Long feedId
    ) {
        List<CommentResponseDto> commentResponseDto = commentService.findAllComment(feedId);
        return new ResponseEntity<>(commentResponseDto, HttpStatus.OK);
    }

    /**
     * 댓글 삭제
     *
     * @param feedId    피드 식별자
     * @param commentId 댓글 식별자
     * @return {@link PostMessageResponseDto} 댓글 삭제 완료 문구
     */
    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<PostMessageResponseDto> deleteComment(
            @PathVariable Long feedId,
            @PathVariable Long commentId
    ) {
        PostMessageResponseDto postMessageResponseDto = commentService.deleteComment(feedId, commentId);
        return new ResponseEntity<>(postMessageResponseDto, HttpStatus.OK);
    }
}
