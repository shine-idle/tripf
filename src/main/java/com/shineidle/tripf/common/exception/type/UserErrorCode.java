package com.shineidle.tripf.common.exception.type;

import com.shineidle.tripf.common.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ExceptionType {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"유저를 찾을 수 없습니다."),
    USER_DEACTIVATED(HttpStatus.NOT_ACCEPTABLE, "탈퇴 처리된 유저입니다."),
    EMAIL_DUPLICATED(HttpStatus.CONFLICT, "중복된 이메일 입니다."),
    PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "토큰을 찾을 수 없습니다, 로그인 해주세요."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료 되었습니다, 다시 로그인 해주세요."),
    ;


    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getErrCode() {
        return this.name();
    }
}
