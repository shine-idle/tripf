package com.shineidle.tripf.payment.service;

import com.shineidle.tripf.order.entity.Order;
import com.shineidle.tripf.payment.dto.PaymentCancelDto;
import com.shineidle.tripf.payment.dto.PaymentConfirmDto;
import com.shineidle.tripf.payment.dto.PaymentDto;
import com.shineidle.tripf.payment.dto.PaymentRequestDto;
import com.shineidle.tripf.payment.entity.Payment;
import org.springframework.transaction.annotation.Transactional;

public interface PaymentService {

    /**
     * 결제 요청
     * @param paymentRequestDto
     * @return
     */
    PaymentDto requestPayment(PaymentRequestDto paymentRequestDto);

    /**
     * 결제 승인
     * @param paymentConfirmDto 결제 승인 정보
     * @return 결제 정보
     */
    PaymentDto confirmPayment(PaymentConfirmDto paymentConfirmDto);

    /**
     * 결제 취소
     * @param paymentCancelDto 결제 취소 정보
     * @return 결제 취소 정보
     */
    PaymentDto cancelPayment(PaymentCancelDto paymentCancelDto);

    /**
     * 결제 정보 조회
     * @param paymentKey 결제 키
     * @return 결제 정보
     */
    PaymentDto getPaymentDetails(String paymentKey);

    /**
     * 결제 취소 정보 조회
     * @param paymentKey 결제 키
     * @return 결제 취소 정보
     */
    PaymentDto getCancelDetails(String paymentKey);

}