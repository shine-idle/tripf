package com.shineidle.tripf.global.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class GlobalException extends RuntimeException {

    private final HttpStatus httpStatus;
    private final String errCode;
    private final String message;

    public GlobalException(ExceptionType exceptionType) {
        super();
        this.httpStatus = exceptionType.getHttpStatus();
        this.errCode = exceptionType.getErrCode();
        this.message = exceptionType.getMessage();
    }
}
