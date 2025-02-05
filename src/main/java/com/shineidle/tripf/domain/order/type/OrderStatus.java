package com.shineidle.tripf.domain.order.type;

// TODO: 사용되지 않는 변수 삭제 유무
public enum OrderStatus {
    /**
     * 주문 접수
     */
    ORDER_RECEIVED,

    /**
     * 배송 준비중
     */
    PREPARING_SHIPPING,

    /**
     * 배송중
     */
    SHIPPED,

    /**
     * 배송 완료
     */
    DELIVERED
}
