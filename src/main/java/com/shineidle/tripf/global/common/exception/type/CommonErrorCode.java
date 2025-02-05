package com.shineidle.tripf.global.common.exception.type;

import com.shineidle.tripf.global.common.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ExceptionType {
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    NEED_ADMIN_ACCESS(HttpStatus.FORBIDDEN, "관리자 권한이 필요합니다."),
    INVALID_USER(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    PAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "페이지가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getErrCode() {
        return this.name();
    }
}
