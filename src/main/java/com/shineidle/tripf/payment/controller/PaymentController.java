package com.shineidle.tripf.payment.controller;

import com.shineidle.tripf.payment.dto.PaymentCancelDto;
import com.shineidle.tripf.payment.dto.PaymentConfirmDto;
import com.shineidle.tripf.payment.dto.PaymentDto;
import com.shineidle.tripf.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    //결제 승인
    @PostMapping("/confirm/{paymentKey}")
    public String approve(
            @RequestBody PaymentConfirmDto paymentConfirmDto,
            Model model
            ) {
        PaymentDto paymentDto = paymentService.confirmPayment(paymentConfirmDto);
        model.addAttribute("payment", paymentDto);
        return "payment/confirmation";

    }

    //결제 취소
    @PostMapping("/cancel")
    public String cancelPayment(
            @RequestBody PaymentCancelDto paymentCancelDto,
            Model model
    ) {
        PaymentDto paymentDto = paymentService.cancelPayment(paymentCancelDto);
        model.addAttribute("payment", paymentDto);
        return "payment/cancel";
    }

    //결제 상세 조회
    @GetMapping("details/{paymentsKey}")
    public String getPaymentDetails(
            @PathVariable String paymentsKey,
            Model model
    ) {
        PaymentDto paymentDto =paymentService.getPaymentDetails(paymentsKey);
        model.addAttribute("payment", paymentDto);
        return "payment/details";
    }

    //결제 취소 상세 조회
    @GetMapping("/cancel/details/{paymentKey}")
    public String getCancelDetails(
            @PathVariable String paymentsKey,
            Model model
    ) {
        PaymentDto cancelDto = paymentService.getCancelDetails(paymentsKey);
        model.addAttribute("payment", cancelDto);
        return "payment/cancelDetails";
    }
}
