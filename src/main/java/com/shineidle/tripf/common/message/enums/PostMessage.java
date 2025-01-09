package com.shineidle.tripf.common.message.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
public enum PostMessage {
    PEED_DELETED("피드가 삭제되었습니다."),
    COMMENT_DELETED("댓글이 삭제되었습니다."),
    PRODUCT_DELETED("상품이 삭제되었습니다.");

    private final String message;
}