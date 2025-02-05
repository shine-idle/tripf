package com.shineidle.tripf.global.common.exception.type;

import com.shineidle.tripf.global.common.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MailErrorCode implements ExceptionType {

    TIME_INVALID(HttpStatus.BAD_REQUEST, "메일 발송시간이 아닙니다."),
    MAIL_EMPTY(HttpStatus.NOT_FOUND, "상위 피드가 없습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getErrCode() {
        return this.name();
    }
}