package com.shineidle.tripf.mail.controller;

import com.shineidle.tripf.mail.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
public class MailController {

    private final MailService mailService;

    /**
     * 상위 피드 이메일 발송 테스트
     */
    @Operation(summary = "상위 피드 이메일 발송 테스트")
    @GetMapping("/sendTopFeedsMail")
    public ResponseEntity<Void> sendTopFeedsMail() {
        mailService.sendTopFeedsMail();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * simple 테스트 메일 발송 API
     *
     * @param email 수신자 이메일
     * @return 성공 메시지
     */
    @Operation(summary = "테스트 메일 발송")
    @GetMapping("/send")
    public ResponseEntity<Void> sendTestMail(
            @RequestParam String email) {
        String subject = "테스트 메일";
        String text = "이것은 테스트 메일입니다. SMTP 설정이 성공적으로 완료되었습니다.";

        mailService.sendSimpleMail(email, subject, text);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
