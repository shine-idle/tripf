package com.shineidle.tripf.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentRequestDto {
    @NotBlank(message = "댓글을 입력해주세요.")
    @Size(max = 255, message = "댓글은 255자 이내로 입력해야 합니다.")
    private final String comment ;
}