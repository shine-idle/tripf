package com.shineidle.tripf.common.exception.type;

import com.shineidle.tripf.common.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProductErrorCode implements ExceptionType {
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 상품을 찾을 수 없습니다."),
    PRODUCT_DUPLICATED(HttpStatus.CONFLICT, "상품명은 중복될 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getErrCode() {
        return this.name();
    }
}
