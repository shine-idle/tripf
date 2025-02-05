package com.shineidle.tripf.global.common.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionType {

    HttpStatus getHttpStatus();

    String getErrCode();

    String getMessage();
}
