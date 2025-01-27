package com.shineidle.tripf.user.controller;

import com.shineidle.tripf.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping
public class AccountViewController {
    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "user/login";
    }

    @GetMapping("/signup")
    public String signUpPage() {
        return "user/signup"; // signup.html 뷰 반환
    }

    @GetMapping("/logout")
    public String logoutPage() {
        return "user/logout"; // logout.html 뷰 반환
    }
}
