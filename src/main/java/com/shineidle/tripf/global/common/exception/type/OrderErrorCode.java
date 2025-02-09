package com.shineidle.tripf.global.common.exception.type;

import com.shineidle.tripf.global.common.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OrderErrorCode implements ExceptionType {
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 않습니다"),
    INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "수량을 1개 이상 선택해 주세요"),
    ORDER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 주문입니다");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getErrCode() {
        return this.name();
    }
}
