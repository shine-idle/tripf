package com.shineidle.tripf.global.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ExceptionResponseDto {

    private final String errCode;

    private final String message;
}
