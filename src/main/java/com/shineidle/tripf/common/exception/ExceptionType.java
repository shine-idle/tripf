package com.shineidle.tripf.common.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionType {

    HttpStatus getHttpStatus();

    String getErrCode();

    String getMessage();
}
