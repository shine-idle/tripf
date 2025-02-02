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
    PHOTO_DELETED("사진이 삭제되었습니다."),
    PRODUCT_DISCONTINUED("상품을 단종처리 하였습니다."),
    SIGNUP_SUCCESS("회원 가입이 완료되었습니다! 이제 모든 서비스를 마음껏 이용해 보세요."),
    LOGOUT_SUCCESS("로그아웃 처리 되었습니다."),
    CHATBOT_QUESTION_SUCCESS("입력하신 새로운 질문이 성공적으로 저장되었습니다."),
    CHATBOT_ANSWER_SUCCESS("입력하신 새로운 답변이 성공적으로 저장되었습니다."),
    PASSWORD_UPDATED("비밀번호가 변경되었습니다. 다시 로그인해주세요."),
    USERNAME_UPDATED("이름이 변경되었습니다."),
    USER_DEACTIVATED("회원 탈퇴가 완료되었습니다."),
    CHATROOM_DELETED("채팅방이 삭제 되었습니다.")
    ;


    private final String message;
}