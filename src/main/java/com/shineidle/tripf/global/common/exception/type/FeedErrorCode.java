package com.shineidle.tripf.global.common.exception.type;

import com.shineidle.tripf.global.common.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FeedErrorCode implements ExceptionType {
    FEED_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 피드를 찾을 수 없습니다."),
    DAYS_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 일정을 찾을 수 없습니다."),
    ACTIVITY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 활동을 찾을 수 없습니다."),
    FEED_CANNOT_ACCESS(HttpStatus.FORBIDDEN,"작성자만 접근이 가능합니다."),
    DATE_INVALID(HttpStatus.BAD_REQUEST, "일정 날짜는 피드의 시작 및 종료 날짜 범위 내에 있어야 합니다."),
    DATE_DUPLICATE(HttpStatus.CONFLICT, "동일한 일정이 이미 존재합니다."),
    FEED_ALREADY_DELETED(HttpStatus.GONE, "피드가 이미 삭제되었습니다."),
    COUNTRY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 국가가 존재하지 않습니다."),
    PAGE_INVALID(HttpStatus.BAD_REQUEST, "페이지는 0보다 큰 수를 입력해야합니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getErrCode() {
        return this.name();
    }
}
