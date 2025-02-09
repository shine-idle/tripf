package com.shineidle.tripf.global.common.exception.type;

import com.shineidle.tripf.global.common.exception.ExceptionType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PhotoErrorCode implements ExceptionType {
    PHOTO_EMPTY(HttpStatus.BAD_REQUEST, "사진을 추가해주세요."),
    PHOTO_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사진을 찾을 수 없습니다."),
    DOMAIN_NOT_FOUND(HttpStatus.NOT_FOUND, "유효하지 않은 도메인 타입입니다."),
    RELATION_INVALID(HttpStatus.NOT_FOUND, "사진이 해당 도메인에 속하지 않습니다."),
    INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "허용되지 않는 파일 형식은 업로드할 수 없습니다. (허용 : 'jpg', 'png', 'jpeg', 'avif')"),
    INVALID_FILE_SIZE(HttpStatus.BAD_REQUEST, "파일 크기가 설정된 제한을 초과하여 업로드할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public String getErrCode() {
        return this.name();
    }
}
