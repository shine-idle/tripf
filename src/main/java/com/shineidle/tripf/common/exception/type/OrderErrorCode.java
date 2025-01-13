package com.shineidle.tripf.common.exception.type;

import com.shineidle.tripf.common.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OrderErrorCode implements ExceptionType {
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "주문을 찾을 수 않습니다"),
    INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "수량을 1개 이상 선택해 주세요");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getErrCode() {
        return this.name();
    }
}
