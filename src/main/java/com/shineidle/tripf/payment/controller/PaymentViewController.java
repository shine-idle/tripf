package com.shineidle.tripf.payment.controller;

import com.shineidle.tripf.payment.dto.PaymentRequestDto;
import com.shineidle.tripf.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/paymentsTest")
public class PaymentViewController {

    private final PaymentService paymentService;

    public PaymentViewController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Value("${toss.client-key}") // application.properties에서 값 불러오기
    private String tossClientKey;

    @GetMapping
    public String showPaymentPageTest(Model model) {
        model.addAttribute("amount", 5000);
        model.addAttribute("tossClientKey", tossClientKey);
        return "paymentTest/paymentTest";
    }

    @PostMapping("/requestTest")
    @ResponseBody
    public Map<String, String> requestPaymentTest(@RequestBody PaymentRequestDto request) {
        return paymentService.createPaymentRequestTest(request.getAmount());
    }

    @GetMapping("/successTest")
    public String showSuccessPageTest(@RequestParam("paymentKey") String paymentKey,
                                      @RequestParam("orderId") String orderId,
                                      @RequestParam("amount") int amount) {
        paymentService.confirmPaymentTest(paymentKey, orderId, amount);
        return "paymentTest/successTest";
    }

    @GetMapping("/failTest")
    public String showFailPageTest() {
        return "paymentTest/failTest";
    }
}
