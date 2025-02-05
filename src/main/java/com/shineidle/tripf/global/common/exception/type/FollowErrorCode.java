package com.shineidle.tripf.global.common.exception.type;

import com.shineidle.tripf.global.common.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FollowErrorCode implements ExceptionType {
    NOT_SELF_FOLLOW(HttpStatus.BAD_REQUEST, "자기 자신을 팔로우할 수 없습니다"),
    ALREADY_FOLLOWED(HttpStatus.BAD_REQUEST, "이미 팔로우한 사용자입니다"),
    NOT_FOUND_FOLLOW(HttpStatus.NOT_FOUND, "팔로우 할 사용자가 존재하지 않습니다"),
    FOLLOW_RELATION_NOT_FOUND(HttpStatus.NOT_FOUND, "팔로우 된 상태가 아닙니다");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getErrCode() {
        return this.name();
    }
}
