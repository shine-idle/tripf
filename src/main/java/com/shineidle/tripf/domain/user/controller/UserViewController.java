package com.shineidle.tripf.domain.user.controller;

import com.shineidle.tripf.domain.user.dto.PasswordUpdateRequestDto;
import com.shineidle.tripf.domain.user.dto.UserRequestDto;
import com.shineidle.tripf.domain.user.dto.UserResponseDto;
import com.shineidle.tripf.domain.user.dto.UsernameUpdateRequestDto;
import com.shineidle.tripf.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// todo 추후 구현 예정

/**
 * 사용자 관련 뷰를 처리하는 컨트롤러
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserViewController {
    private final UserService userService;

    /**
     * 사용자 정보를 조회하여 뷰에 전달
     *
     * @param userId 유저 Id
     * @param model  뷰에 전달할 모델
     * @return 사용자 정보를 표시할 뷰 이름
     */
    @GetMapping("/{userId}")
    public String findUser(@PathVariable Long userId, Model model) {
        UserResponseDto user = userService.findUser(userId);
        model.addAttribute("user", user);
        return "user/view";
    }

    /**
     * 사용자 인증 폼을 반환
     *
     * @return 사용자 인증 폼 뷰 이름
     */
    @GetMapping("/verify")
    public String verifyForm() {
        return "user/verify-form";
    }

    /**
     * 사용자가 입력한 인증 정보를 처리, 프로필 페이지로 리다이렉션
     *
     * @param dto 사용자 인증 요청 데이터
     * @return 프로필 페이지로 리다이렌션
     */
    @PostMapping("/verify")
    public String verify(@ModelAttribute UserRequestDto dto) {
        userService.verify(dto);
        return "redirect:/users/profile";
    }

    /**
     * 비밀번호 수정 폼 반환
     *
     * @return 비밀번호 수정 폼
     */
    @GetMapping("/edit-password")
    public String editPasswordForm() {
        return "user/edit-password-form";
    }

    /**
     * 비밀번호 업데이트 후 프로필 페이지로 리다이렉트
     *
     * @param dto 비밀번호 변경 요청 데이터
     * @return 프로필 페이지로 리다이렉트
     */
    @PostMapping("/edit-password")
    public String updatePassword(@ModelAttribute PasswordUpdateRequestDto dto) {
        userService.updatePassword(dto);
        return "redirect:/users/profile";
    }

    /**
     * 사용자 이름 수정 폼을 반환
     *
     * @return 사용자 이름 수정 폼
     */
    @GetMapping("/edit-name")
    public String editNameForm() {
        return "user/edit-name-form";
    }

    /**
     * 사용자 이름 업데이트 후, 프로필 페이지로 리다이렉트
     *
     * @param dto 사용자 이름 변경 요청 데이터
     * @return 프로필 페이지로 리다이렉트
     */
    @PostMapping("/edit-name")
    public String updateName(@ModelAttribute UsernameUpdateRequestDto dto) {
        userService.updateName(dto);
        return "redirect:/users/profile";
    }

    /**
     * 사용자 탈퇴 폼을 반환
     *
     * @return 사용자 탈퇴 폼
     */
    @GetMapping("/deactivate")
    public String deactivateForm() {
        return "user/deactivate-form";
    }

    /**
     * 계정 탈퇴처리, 로그아웃 페이지로 리다이렉트
     *
     * @param dto 사용자 인증 요청 데이터
     * @return 로그아웃 페이지로 리다이렉트
     */
    @PostMapping("/deactivate")
    public String deleteUser(@ModelAttribute UserRequestDto dto) {
        userService.deleteUser(dto);
        return "redirect:/logout";
    }
}
