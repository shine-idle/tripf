package com.shineidle.tripf.user.controller;

import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.common.message.enums.PostMessage;
import com.shineidle.tripf.user.dto.JwtResponseDto;
import com.shineidle.tripf.user.dto.UserRequestDto;
import com.shineidle.tripf.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountController {
    private final UserService userService;

    /**
     * 회원가입
     * @param dto {@link UserRequestDto}
     * @return {@link PostMessageResponseDto} 회원가입 완료 문구
     */
    @PostMapping("/signup")
    public ResponseEntity<PostMessageResponseDto> signUp(
            @RequestBody UserRequestDto dto
    ) {
        return new ResponseEntity<>(userService.createUser(dto), HttpStatus.CREATED);
    }

    /**
     * 로그인
     * @param dto {@link UserRequestDto}
     * @return {@link JwtResponseDto} 토큰 정보
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(
            @RequestBody UserRequestDto dto
    ) {
        return new ResponseEntity<>(userService.login(dto), HttpStatus.OK);
    }

    /**
     * 로그아웃
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param authentication {@link Authentication}
     * @return {@link PostMessageResponseDto} 로그아웃 완료 문구
     */
    @PostMapping("/logout")
    public ResponseEntity<PostMessageResponseDto> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);

            log.info("인증 객체의 삭제 확인: {}", SecurityContextHolder.getContext().getAuthentication() == null);
            return new ResponseEntity<>(new PostMessageResponseDto(PostMessage.LOGOUT_SUCCESS), HttpStatus.OK);
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "잘못된 접근입니다.");
    }
}
