package com.shineidle.tripf.common.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthenticationScheme {
    BEARER("Bearer");

    private final String name;

    /**
     * Authorization 헤더의 고정값으로 생성
     * @param authenticationScheme {@link AuthenticationScheme}
     * @return 생성된 고정값
     */
    public static String generateType(AuthenticationScheme authenticationScheme) {
        return authenticationScheme.getName() + " ";
    }
}
