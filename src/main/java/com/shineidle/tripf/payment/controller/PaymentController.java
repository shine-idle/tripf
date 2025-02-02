package com.shineidle.tripf.payment.controller;

import com.shineidle.tripf.order.entity.Order;
import com.shineidle.tripf.payment.dto.PaymentCancelDto;
import com.shineidle.tripf.payment.dto.PaymentConfirmDto;
import com.shineidle.tripf.payment.dto.PaymentDto;
import com.shineidle.tripf.payment.dto.PaymentRequestDto;
import com.shineidle.tripf.payment.entity.Payment;
import com.shineidle.tripf.payment.repository.PaymentRepository;
import com.shineidle.tripf.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {


    private final PaymentService paymentService;
    private final PaymentRepository paymentRepository;

    /**
     * 결제 요청을 처리하는 메서드
     * 결제 요청 DTO를 받아서 결제 정보를 처리하고, 결제 확인 화면을 반환
     *
     * @param paymentRequestDto 결제 요청 데이터
     * @param model 결제 정보 모델
     * @return 결제 확인 화면의 경로
     */
    @PostMapping("/request")
    public String requestPayment(
            @ModelAttribute PaymentRequestDto paymentRequestDto,
            Model model
    ) {
        PaymentDto paymentDto = paymentService.requestPayment(paymentRequestDto);
        model.addAttribute("payment", paymentDto);
        return "payment/confirmation";
    }

    /**
     * 결제 확인을 처리하는 메서드
     * 결제 키와 주문 ID를 받아 결제를 승인하고, 결제 확인 화면을 반환
     *
     * @param paymentKey 결제 키
     * @param orderId 주문 ID
     * @param model 결제 정보 모델
     * @return 결제 확인 화면의 경로
     */
    @PostMapping("/confirm")
    public String confirmPayment(
            @RequestParam String paymentKey,
            @RequestParam String orderId,
            Model model
    ) {    // 주문 ID로 결제 정보를 가져오기
        Payment payment = paymentRepository.findByPaymentKey(paymentKey)
                .orElseThrow(() -> new IllegalArgumentException("결제 정보가 없습니다"));

        // 주문 금액을 Payment 엔티티의 getAmount() 메서드로 가져오기
        Long amount = payment.getAmount();

        PaymentConfirmDto paymentConfirmDto = new PaymentConfirmDto(paymentKey, orderId, amount);

        // 결제 승인 처리
        PaymentDto paymentDto = paymentService.confirmPayment(paymentConfirmDto);
        model.addAttribute("payment", paymentDto);
        return "payConfirmation";
    }

    /**
     * 결제 취소를 처리하는 메서드
     * 결제 취소 정보를 받아 결제를 취소하고, 취소된 결제 정보 화면을 반환
     *
     * @param paymentCancelDto 결제 취소 데이터
     * @param model 취소된 결제 정보 모델
     * @return 결제 취소 화면의 경로
     */
    @PostMapping("/cancel")
    public String cancelPayment(
            @RequestBody PaymentCancelDto paymentCancelDto,
            Model model
    ) {
        PaymentDto paymentDto = paymentService.cancelPayment(paymentCancelDto);
        model.addAttribute("payment", paymentDto);
        return "payment/cancel";
    }

    /**
     * 결제 상세 정보를 조회하는 메서드
     * 결제 키를 받아 해당 결제의 상세 정보를 반환
     *
     * @param paymentKey 결제 키
     * @param model 결제 상세 정보 모델
     * @return 결제 상세 정보 화면의 경로
     */
    @GetMapping("/details/{paymentKey}")
    public String getPaymentDetails(
            @PathVariable String paymentKey,
            Model model
    ) {
        PaymentDto paymentDto = paymentService.getPaymentDetails(paymentKey);
        model.addAttribute("payment", paymentDto);
        return "payment/details";
    }

    /**
     * 결제 취소 상세 정보를 조회하는 메서드
     * 결제 취소 키를 받아 해당 결제 취소의 상세 정보를 반환
     *
     * @param paymentKey 결제 키
     * @param model 결제 취소 상세 정보 모델
     * @return 결제 취소 상세 정보 화면의 경로
     */
    @GetMapping("/cancel/details/{paymentKey}")
    public String getCancelDetails(
            @PathVariable String paymentKey,
            Model model
    ) {
        PaymentDto cancelDto = paymentService.getCancelDetails(paymentKey);
        model.addAttribute("payment", cancelDto);
        return "payment/cancelDetails";
    }
}