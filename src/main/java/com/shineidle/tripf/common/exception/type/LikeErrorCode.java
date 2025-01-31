package com.shineidle.tripf.common.exception.type;

import com.shineidle.tripf.common.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum LikeErrorCode implements ExceptionType {
    ALREADY_LIKE(HttpStatus.BAD_REQUEST,"이미 좋아요를 눌렀습니다"),
    LIKED_YET(HttpStatus.BAD_REQUEST, "좋아요를 먼저 해주세요");


    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getErrCode() {
        return this.name();
    }
}
