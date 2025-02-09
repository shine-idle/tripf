package com.shineidle.tripf.domain.mail.controller;

import com.shineidle.tripf.domain.mail.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
public class MailController {
    private final MailService mailService;

    /**
     * 상위 피드 이메일 발송
     * 매주 월요일 오후 2시 메일 발송
     * 락을 통해 한 서버에서만 실행되도록 설정
     *
     * @return Void
     */
    @Operation(summary = "상위 피드 이메일 정기 발송")
    @GetMapping("/sendTopFeedsMail")
    public ResponseEntity<Void> sendTopFeedsMail() {
        mailService.sendTopFeedsMail();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 즉시 메일 발송 API
     *
     * @return Void
     */
    @Operation(summary = "즉시 메일 발송")
    @PostMapping("/sendTopFeedsMailNow")
    public ResponseEntity<Void> sendTopFeedsMailNow() {
        mailService.sendTopFeedsMailNow();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
