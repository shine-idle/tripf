package com.shineidle.tripf.payment.service;

import com.shineidle.tripf.payment.dto.PaymentCancelDto;
import com.shineidle.tripf.payment.dto.PaymentConfirmDto;
import com.shineidle.tripf.payment.dto.PaymentDto;

public interface PaymentService {
    // 결제 승인
    PaymentDto confirmPayment(PaymentConfirmDto paymentConfirmDto);

    // 결제 취소
    PaymentDto cancelPayment(PaymentCancelDto paymentCancelDto);

    // 결제 정보 조회
    PaymentDto getPaymentDetails(String paymentKey);

    // 결제 취소 정보 조회
    PaymentDto getCancelDetails(String paymentKey);
}