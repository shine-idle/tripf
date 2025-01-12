package com.shineidle.tripf.comment.controller;

import com.shineidle.tripf.comment.dto.CommentRequestDto;
import com.shineidle.tripf.comment.dto.CommentResponseDto;
import com.shineidle.tripf.comment.service.CommentService;
import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;
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
     */
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
     */
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
     */
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> findAllComment(
            @PathVariable Long feedId
    ) {
        List<CommentResponseDto> commentResponseDto = commentService.findAllComment(feedId);
        return new ResponseEntity<>(commentResponseDto, HttpStatus.OK);
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<PostMessageResponseDto> deleteComment(
            @PathVariable Long feedId,
            @PathVariable Long commentId
    ) {
        PostMessageResponseDto postMessageResponseDto = commentService.deleteComment(feedId,commentId);
        return new ResponseEntity<>(postMessageResponseDto, HttpStatus.OK);
    }

}
