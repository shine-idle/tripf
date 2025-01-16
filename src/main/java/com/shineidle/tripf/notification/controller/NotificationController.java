package com.shineidle.tripf.notification.controller;

import com.shineidle.tripf.config.auth.UserDetailsImpl;
import com.shineidle.tripf.notification.dto.NotificationResponseDto;
import com.shineidle.tripf.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * 알림 목록 조회
     *
     * @param userDetails {@link UserDetailsImpl}
     * @return {@link NotificationResponseDto} 알림 응답 Dto
     */
    @Operation(summary = "알림 목록 조회")
    @GetMapping
    public ResponseEntity<List<NotificationResponseDto>> findNotification(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Long userId = userDetails.getUserId();
        List<NotificationResponseDto> notificationResponseDto = notificationService.findNotification(userId);
        return new ResponseEntity<>(notificationResponseDto, HttpStatus.OK);
    }
}
