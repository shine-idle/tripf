package com.shineidle.tripf.common.message.dto;

import com.shineidle.tripf.common.message.enums.PostMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class PostMessageResponseDto {

    private final String message;

    public PostMessageResponseDto(PostMessage postMessage) {
        this.message = postMessage.getMessage();
    }
}