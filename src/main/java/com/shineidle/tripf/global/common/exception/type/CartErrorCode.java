package com.shineidle.tripf.global.common.exception.type;

import com.shineidle.tripf.global.common.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CartErrorCode implements ExceptionType {
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "로그인 한 유저의 장바구니에서 해당 상품을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getErrCode() {
        return this.name();
    }
}
