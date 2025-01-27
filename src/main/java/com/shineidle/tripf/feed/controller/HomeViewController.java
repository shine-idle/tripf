package com.shineidle.tripf.feed.controller;

import com.shineidle.tripf.feed.dto.HomeResponseDto;
import com.shineidle.tripf.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/home")
public class HomeViewController {

    private final FeedService feedService;

    /**
     * 홈페이지 View 반환
     */
    @GetMapping
    public String home(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
            HomeResponseDto homeResponseDto = feedService.findHomeData();
            model.addAttribute("homeData", homeResponseDto);
            return "home/home-logged-in"; // 로그인 사용자용 뷰
        } else {
            HomeResponseDto publicHomeResponseDto = feedService.findPublicHomeData();
            model.addAttribute("homeData", publicHomeResponseDto);
            return "home/home-guest"; // 비로그인 사용자용 뷰
        }
    }
}