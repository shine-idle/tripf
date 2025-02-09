package com.shineidle.tripf.domain.payment.controller;

import com.shineidle.tripf.domain.payment.dto.PaymentRequestDto;
import com.shineidle.tripf.domain.payment.service.PaymentService;
import com.shineidle.tripf.domain.payment.service.PaymentServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/payments")
public class PaymentViewController {
    private final PaymentService paymentService;

    public PaymentViewController(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * PaymentViewController 생성자
     */
    @Value("${toss.client-key}")
    private String tossClientKey;

    /**
     * 결제 페이지 보여줌
     *
     * @param model 결제 금액과 Toss 클라이언트 키 전달
     * @return "payment/payment"
     */
    @GetMapping
    public String showPaymentPage(Model model) {
        model.addAttribute("amount", 5000);
        model.addAttribute("tossClientKey", tossClientKey);
        return "payment/payment";
    }

    /**
     * 결제 요청 처리
     *
     * @param request PaymentRequestDto
     * @return 결제 요청에 대한 정보를 담은 객체
     */
    @PostMapping("/request")
    @ResponseBody
    public Map<String, String> requestPayment(
            @RequestBody PaymentRequestDto request
    ) {
        return paymentService.createPaymentRequest(request.getAmount());
    }

    /**
     *결제 성공 후 호출되는 메소드
     *
     * @param paymentKey 결제키
     * @param orderId 주문 ID
     * @param amount 결제 금액
     * @return "payment/success"
     */
    @GetMapping("/success")
    public String showSuccessPage(@RequestParam("paymentKey") String paymentKey,
                                  @RequestParam("orderId") String orderId,
                                  @RequestParam("amount") int amount
    ) {
        paymentService.confirmPayment(paymentKey, orderId, amount);

        return "payment/success";
    }

    /**
     * 결제 호출 후 호출되는 메소드
     *
     * @return "payment/fail"
     */
    @GetMapping("/fail")
    public String showFailPage() {
        return "payment/fail";
    }
}