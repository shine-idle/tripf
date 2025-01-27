package com.shineidle.tripf.payment.service;

import com.shineidle.tripf.payment.dto.PaymentCancelDto;
import com.shineidle.tripf.payment.dto.PaymentConfirmDto;
import com.shineidle.tripf.payment.dto.PaymentDto;
import com.shineidle.tripf.payment.entity.Payment;
import com.shineidle.tripf.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    //결제 승인
    @Override
    public PaymentDto confirmPayment(PaymentConfirmDto paymentConfirmDto) {
        Payment payment = paymentRepository.findByPaymentKey(paymentConfirmDto.getPaymentKey())
                .orElseThrow(() -> new IllegalArgumentException(""));

        payment.approvedPayment(LocalDateTime.now());
        paymentRepository.save(payment);

        return new PaymentDto(payment);
    }

    //결제 취소
    @Override
    public PaymentDto cancelPayment(PaymentCancelDto paymentCancelDto) {
        Payment payment = paymentRepository.findByPaymentKey(paymentCancelDto.getPaymentKey())
                .orElseThrow(() -> new IllegalArgumentException());

        payment.canceledPayment(LocalDateTime.now());
        paymentRepository.save(payment);

        return new PaymentDto(payment);
    }

    //결제정보 조회
    @Override
    public PaymentDto getPaymentDetails(String paymentKey) {
        Payment payment = paymentRepository.findByPaymentKey(paymentKey)
                .orElseThrow(() -> new IllegalArgumentException(""));

        return new PaymentDto(payment);
    }

    //결제 취소 정보 조회
    @Override
    public PaymentDto getCancelDetails(String paymentkey) {
        Payment payment = paymentRepository.findByPaymentKey(paymentkey)
                .orElseThrow(() -> new IllegalArgumentException());

        return new PaymentDto(payment);
    }

}
