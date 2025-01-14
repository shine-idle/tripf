package com.shineidle.tripf.common.exception.type;

import com.shineidle.tripf.common.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PhotoErrorCode implements ExceptionType {
    PHOTO_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사진을 찾을 수 없습니다."),
    DOMAIN_NOT_FOUND(HttpStatus.NOT_FOUND, "유효하지 않은 도메인 타입입니다."),
    RELATION_INVALID(HttpStatus.NOT_FOUND, "사진이 해당 도메인에 속하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getErrCode() {
        return this.name();
    }
}
