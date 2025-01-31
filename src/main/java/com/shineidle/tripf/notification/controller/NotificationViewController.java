package com.shineidle.tripf.notification.controller;

import com.shineidle.tripf.config.auth.UserDetailsImpl;
import com.shineidle.tripf.notification.dto.NotificationResponseDto;
import com.shineidle.tripf.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationViewController {

    private final NotificationService notificationService;

    @GetMapping
    public String getNotifications(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        if (userDetails == null) {
            model.addAttribute("loginRequired", true); // 로그인 필요 플래그 추가
            model.addAttribute("notifications", null); // 알림 데이터 없음
            return "header"; // header.html 렌더링
        }

        Long userId = userDetails.getUserId();
        List<NotificationResponseDto> notifications = notificationService.findNotification(userId);
        model.addAttribute("notifications", notifications);
        model.addAttribute("loginRequired", false); // 로그인된 상태

        return "fragments/header";
    }
}
