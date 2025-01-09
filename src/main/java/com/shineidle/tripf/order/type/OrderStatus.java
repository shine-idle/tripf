package com.shineidle.tripf.order.type;

public enum OrderStatus {
    ORDER_RECEIVED, //주문 접수
    PREPARING_SHIPPING, //배송 준비중
    SHIPPED, //배송중
    DELIVERED//배송 완료
}
