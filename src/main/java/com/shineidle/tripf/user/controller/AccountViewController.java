package com.shineidle.tripf.user.controller;

import com.shineidle.tripf.common.exception.GlobalException;
import com.shineidle.tripf.user.dto.UserRequestDto;
import com.shineidle.tripf.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping
public class AccountViewController {
    private final UserService userService;

    @GetMapping("/signup")
    public String signUpPage() {
        return "user/signup"; // signup.html 뷰 반환
    }

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

    @GetMapping("/login")
    public String loginPage() {
        return "user/login";
    }

    @PostMapping("/login")
    public String login(
            @ModelAttribute UserRequestDto dto,
            Model model
    ) {
        try {
            userService.login(dto);
            model.addAttribute("message", "로그인 성공!");
            return "home/home-logged-in";
        } catch (GlobalException e) {
            model.addAttribute("error", e.getMessage());
            return "user/login";
        }
    }

    @GetMapping("/logout")
    public String logoutPage() {

        return "user/logout"; // logout.html 뷰 반환
    }
}
