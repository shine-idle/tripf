package com.shineidle.tripf.paymentTest.controller;

import com.shineidle.tripf.paymentTest.dto.PaymentRequestDtoTest;
import com.shineidle.tripf.paymentTest.service.PaymentServiceTest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/paymentsTest")
public class PaymentViewControllerTest {

    private final PaymentServiceTest paymentServiceTest;

    public PaymentViewControllerTest(PaymentServiceTest paymentServiceTest) {
        this.paymentServiceTest = paymentServiceTest;
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
    public Map<String, String> requestPaymentTest(@RequestBody PaymentRequestDtoTest request) {
        return paymentServiceTest.createPaymentRequestTest(request.getAmount());
    }

    @GetMapping("/successTest")
    public String showSuccessPageTest(@RequestParam("paymentKey") String paymentKey,
                                      @RequestParam("orderId") String orderId,
                                      @RequestParam("amount") int amount) {
        paymentServiceTest.confirmPaymentTest(paymentKey, orderId, amount);
        return "paymentTest/successTest";
    }

    @GetMapping("/failTest")
    public String showFailPageTest() {
        return "paymentTest/failTest";
    }
}
