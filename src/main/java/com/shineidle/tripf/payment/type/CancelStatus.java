package com.shineidle.tripf.payment.type;

/**
 * 결제 취소 상태를 나타내는 enum.
 * <p>
 * 각 항목은 결제 취소와 관련된 다양한 상태를 나타냅니다.
 * </p>
 */
public enum CancelStatus {
    /**
     * 결제 취소 요청이 대기 중인 상태
     */
    PENDING,

    /**
     * 결제 취소가 완료된 상태
     */
    CANCELLED,

    /**
     * 결제 취소가 실패한 상태
     */
    FAILED
}
