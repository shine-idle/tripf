package com.shineidle.tripf.payment.controller;

import com.shineidle.tripf.payment.dto.PaymentRequestDto;
import com.shineidle.tripf.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/payments")
public class PaymentViewController {

    private final PaymentService paymentService;

    public PaymentViewController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Value("${toss.client-key}") // application.properties에서 값 불러오기
    private String tossClientKey;

    @GetMapping
    public String showPaymentPage(Model model) {
        model.addAttribute("amount", 5000);
        model.addAttribute("tossClientKey", tossClientKey);
        return "payment/payment";
    }

    @PostMapping("/request")
    @ResponseBody
    public Map<String, String> requestPayment(@RequestBody PaymentRequestDto request) {
        return paymentService.createPaymentRequest(request.getAmount());
    }

    @GetMapping("/success")
    public String showSuccessPage(@RequestParam("paymentKey") String paymentKey,
                                      @RequestParam("orderId") String orderId,
                                      @RequestParam("amount") int amount) {
        paymentService.confirmPayment(paymentKey, orderId, amount);
        return "payment/success";
    }

    @GetMapping("/fail")
    public String showFailPage() {
        return "payment/fail";
    }
}
