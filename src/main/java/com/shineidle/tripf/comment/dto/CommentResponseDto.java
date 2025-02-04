package com.shineidle.tripf.comment.dto;

import com.shineidle.tripf.comment.entity.Comment;
import com.shineidle.tripf.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CommentResponseDto {
    private final Long id;

    private final String name;

    private final String comment;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    public static CommentResponseDto toDto(final Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getUser().getName(),
                comment.getComment(),
                comment.getCreatedAt(),
                comment.getModifiedAt()
        );
    }
}