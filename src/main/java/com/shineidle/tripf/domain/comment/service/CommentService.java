package com.shineidle.tripf.domain.comment.service;

import com.shineidle.tripf.domain.comment.dto.CommentRequestDto;
import com.shineidle.tripf.domain.comment.dto.CommentResponseDto;
import com.shineidle.tripf.global.common.message.dto.PostMessageResponseDto;
import jakarta.validation.Valid;

import java.util.List;

public interface CommentService {
    /**
     * 댓글 작성
     */
    CommentResponseDto createComment(Long feedId, @Valid CommentRequestDto commentRequestDto);

    /**
     * 댓글 수정
     */
    CommentResponseDto updateComment(Long feedId, Long commentId, @Valid CommentRequestDto commentRequestDto);

    /**
     * 댓글 조회
     */
    List<CommentResponseDto> findAllComment(Long feedId);

    /**
     * 댓글 삭제
     */
    PostMessageResponseDto deleteComment(Long feedId, Long commentId);
}
