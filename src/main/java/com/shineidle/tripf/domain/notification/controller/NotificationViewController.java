package com.shineidle.tripf.domain.notification.controller;

import com.shineidle.tripf.global.security.auth.UserDetailsImpl;
import com.shineidle.tripf.domain.notification.dto.NotificationResponseDto;
import com.shineidle.tripf.domain.notification.service.NotificationService;
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

    /**
     * 알림 조회 view를 반환합니다.
     *
     * @param userDetails 로그인한 사용자 정보 (null일 경우 로그인 필요)
     * @param model       뷰에 전달할 데이터
     * @return 알림이 포함된 헤더 뷰 (로그인 상태에 따라 다름)
     */
    @GetMapping
    public String getNotifications(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            Model model
    ) {
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
