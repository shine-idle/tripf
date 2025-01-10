package com.shineidle.tripf.common.exception.type;

import com.shineidle.tripf.common.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FeedErrorCode implements ExceptionType {
    FEED_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 피드를 찾을 수 없습니다."),
    FEED_CANNOT_ACCESS(HttpStatus.FORBIDDEN,"작성자만 접근이 가능합니다."),
    FEED_ALREADY_DELETED(HttpStatus.GONE, "피드가 이미 삭제되었습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getErrCode() {
        return this.name();
    }
}
