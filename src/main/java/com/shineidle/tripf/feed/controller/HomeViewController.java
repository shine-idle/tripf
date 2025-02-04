package com.shineidle.tripf.feed.controller;

import com.shineidle.tripf.comment.dto.CommentResponseDto;
import com.shineidle.tripf.common.util.JwtProvider;
import com.shineidle.tripf.feed.dto.HomeResponseDto;
import com.shineidle.tripf.feed.service.FeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
//@RequestMapping("/home")
public class HomeViewController {

    private final FeedService feedService;
    private final JwtProvider jwtProvider;

    /**
     * 홈페이지 View 반환
     */
//    @GetMapping
//    public String home(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication != null && authentication.isAuthenticated() && !(authentication.getPrincipal() instanceof String)) {
//            HomeResponseDto homeResponseDto = feedService.findHomeData();
//            model.addAttribute("homeData", homeResponseDto);
//            return "home/home-logged-in"; // 로그인 사용자용 뷰
//        } else {
//            HomeResponseDto publicHomeResponseDto = feedService.findPublicHomeData();
//            model.addAttribute("homeData", publicHomeResponseDto);
//            return "home/home-guest"; // 비로그인 사용자용 뷰
//        }
//    }

//    @GetMapping("/")
//    public String home(Model model) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        log.info(authentication.getName());
//
//        if (authentication.getName() != null && !authentication.getName().equals("anonymousUser")) { // 로그인된 사용자라면
//            HomeResponseDto homeResponseDto = feedService.findHomeData();
//            model.addAttribute("username", authentication.getName());
//            model.addAttribute("homeData", homeResponseDto);
//            return "/home/home-logged-in"; // home-logged-in.html로 이동
//        }
//        HomeResponseDto publicHomeResponseDto = feedService.findPublicHomeData();
//        model.addAttribute("homeData", publicHomeResponseDto);
//        return "/home/home-guest"; // home.html로 이동
//    }

    /**
     * 홈페이지 View를 반환합니다
     *
     * @param token 토큰
     * @param model 모델
     * @return {@link /home/home-guest}
     */
    @GetMapping("/")
    public String home(
            @CookieValue(value = "Authorization", required = false) String token, // JWT 토큰 쿠키에서 가져오기
            Model model
    ) {
        if (token != null && jwtProvider.validToken(token)) { // JWT가 존재하고 유효하면
            Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication); // 인증 객체 설정

            HomeResponseDto homeResponseDto = feedService.findHomeData();
            model.addAttribute("username", authentication.getName());
            model.addAttribute("homeData", homeResponseDto);
            return "home/home-guest"; // 로그인된 사용자용 뷰
        }

        HomeResponseDto publicHomeResponseDto = feedService.findPublicHomeData();
        model.addAttribute("homeData", publicHomeResponseDto);
        return "home/home-guest"; // 비로그인 사용자용 뷰
    }
}