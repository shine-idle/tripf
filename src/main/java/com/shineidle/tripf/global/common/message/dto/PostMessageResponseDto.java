package com.shineidle.tripf.global.common.message.dto;

import com.shineidle.tripf.global.common.message.type.PostMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PostMessageResponseDto {

    private final String message;

    public PostMessageResponseDto(PostMessage postMessage) {
        this.message = postMessage.getMessage();
    }
}