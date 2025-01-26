package com.shineidle.tripf.user.controller;

import com.shineidle.tripf.user.dto.PasswordUpdateRequestDto;
import com.shineidle.tripf.user.dto.UserRequestDto;
import com.shineidle.tripf.user.dto.UserResponseDto;
import com.shineidle.tripf.user.dto.UsernameUpdateRequestDto;
import com.shineidle.tripf.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserViewController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public String findUser(@PathVariable Long userId, Model model) {
        UserResponseDto user = userService.findUser(userId);
        model.addAttribute("user", user);
        return "user/view";
    }

    @GetMapping("/verify")
    public String verifyForm() {
        return "user/verify-form";
    }

    @PostMapping("/verify")
    public String verify(@ModelAttribute UserRequestDto dto) {
        userService.verify(dto);
        return "redirect:/users/profile";
    }

    @GetMapping("/edit-password")
    public String editPasswordForm() {
        return "user/edit-password-form";
    }

    @PostMapping("/edit-password")
    public String updatePassword(@ModelAttribute PasswordUpdateRequestDto dto) {
        userService.updatePassword(dto);
        return "redirect:/users/profile";
    }

    @GetMapping("/edit-name")
    public String editNameForm() {
        return "user/edit-name-form";
    }

    @PostMapping("/edit-name")
    public String updateName(@ModelAttribute UsernameUpdateRequestDto dto) {
        userService.updateName(dto);
        return "redirect:/users/profile";
    }

    @GetMapping("/deactivate")
    public String deactivateForm() {
        return "user/deactivate-form";
    }

    @PostMapping("/deactivate")
    public String deleteUser(@ModelAttribute UserRequestDto dto) {
        userService.deleteUser(dto);
        return "redirect:/logout";
    }
}
