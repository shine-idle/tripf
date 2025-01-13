package com.shineidle.tripf.mail.controller;

import com.shineidle.tripf.mail.service.MailService;
import lombok.RequiredArgsConstructor;
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
     * 테스트 메일 발송 API
     * @param email 수신자 이메일
     * @return 성공 메시지
     */
    @GetMapping("/send")
    public ResponseEntity<String> sendTestMail(@RequestParam String email) {
        String subject = "테스트 메일";
        String text = "이것은 테스트 메일입니다. SMTP 설정이 성공적으로 완료되었습니다.";

        mailService.sendSimpleMail(email, subject, text);
        return ResponseEntity.ok("테스트 메일이 성공적으로 발송되었습니다.");
    }
}
