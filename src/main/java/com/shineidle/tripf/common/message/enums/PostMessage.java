package com.shineidle.tripf.common.message.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public enum PostMessage {
    PEED_DELETED("피드가 삭제되었습니다."),
    COMMENT_DELETED("댓글이 삭제되었습니다."),
    PRODUCT_DELETED("상품이 삭제되었습니다."),
    SIGNUP_SUCCESS("회원 가입이 완료되었습니다! 이제 모든 서비스를 마음껏 이용해 보세요."),
    LOGOUT_SUCCESS("로그아웃 처리 되었습니다."),
    ;


    private final String message;
}