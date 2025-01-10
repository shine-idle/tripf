package com.shineidle.tripf.common.message.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public enum PostMessage {
    PEED_DELETED("피드가 삭제되었습니다."),
    DAYS_DELETED("일정이 삭제되었습니다."),
    ACTIVITY_DELETED("활동이 삭제되었습니다."),
    COMMENT_DELETED("댓글이 삭제되었습니다."),
    PRODUCT_DELETED("상품이 삭제되었습니다."),
    SIGNUP_SUCCESS("회원 가입이 완료되었습니다! 이제 모든 서비스를 마음껏 이용해 보세요."),
    LOGOUT_SUCCESS("로그아웃 처리 되었습니다."),
    PASSWORD_UPDATED("비밀번호가 변경되었습니다. 다시 로그인해주세요."),
    USERNAME_UPDATED("이름이 변경되었습니다.")
    ;


    private final String message;
}