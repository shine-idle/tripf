package com.shineidle.tripf.domain.user.controller;

import com.shineidle.tripf.global.common.exception.GlobalException;
import com.shineidle.tripf.domain.user.dto.UserRequestDto;
import com.shineidle.tripf.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// TODO : 예외처리 컨트롤러로 처리 필요

/**
 * 계정 관련 뷰 컨트롤러
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping
public class AccountViewController {
    private final UserService userService;

    /**
     * 회원가입 페이지 이동
     *
     * @return 회원가입 페이지 (user/signup.html)
     */
    @GetMapping("/signup")
    public String signUpPage() {
        return "user/signup";
    }

    /**
     * 회원가입 처리
     *
     * @param dto   회원가입 요청 데이터
     * @param model 뷰 모델 객체
     * @return 회원가입 성공 시 성공 페이지, 실패 시 회원가입 페이지 반환
     */
    @PostMapping("/signup")
    public String signUp(
            @ModelAttribute UserRequestDto dto,
            Model model
    ) {
        try {
            userService.createUser(dto);
            model.addAttribute("message", "회원가입이 성공적으로 완료되었습니다.");
            return "user/signup-success";
        } catch (GlobalException e) {
            model.addAttribute("error", e.getMessage());
            return "user/signup";
        }
    }

    /**
     * 로그인 페이지 이동
     *
     * @return 로그인 페이지 (user/login.html)
     */
    @GetMapping("/login")
    public String loginPage() {
        return "user/login";
    }

    /**
     * 로그인 처리
     *
     * @param dto                로그인 요청 데이터
     * @param response           HTTP 응답 객체
     * @param redirectAttributes 리다이렉트 시 메시지 전달
     * @return 로그인 성공 시 메인 페이지로 리다이렉트, 실패 시 로그인 페이지로 리다이렉트
     */
    @PostMapping("/login")
    public String login(
            @ModelAttribute UserRequestDto dto,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes
    ) {
        try {
            userService.login(dto, response);
            redirectAttributes.addFlashAttribute("message", "로그인 성공!");
            return "redirect:/";
        } catch (GlobalException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        }
    }

    /**
     * 로그아웃 페이지 이동
     *
     * @return 로그아웃 페이지 (user/logout.html)
     */
    @GetMapping("/logout")
    public String logoutPage() {
        return "user/logout";
    }
}
