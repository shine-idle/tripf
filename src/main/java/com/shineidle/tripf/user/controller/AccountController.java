package com.shineidle.tripf.user.controller;

import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.common.message.enums.PostMessage;
import com.shineidle.tripf.user.dto.JwtResponseDto;
import com.shineidle.tripf.user.dto.UserRequestDto;
import com.shineidle.tripf.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccountController {
    private final UserService userService;

    /**
     * 회원가입
     *
     * @param dto {@link UserRequestDto} 유저 요청 Dto
     * @return {@link PostMessageResponseDto} 회원가입 완료 문구
     */
    @Operation(summary = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<PostMessageResponseDto> signUp(
            @RequestBody UserRequestDto dto
    ) {
        return new ResponseEntity<>(userService.createUser(dto), HttpStatus.CREATED);
    }

    /**
     * 로그인
     *
     * @param dto {@link UserRequestDto} 유저 요청 Dto
     * @return {@link JwtResponseDto} 토큰 정보
     */
    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(
            @RequestBody UserRequestDto dto,
            HttpServletResponse response
    ) {
        return new ResponseEntity<>(userService.login(dto, response), HttpStatus.OK);
    }

    /**
     * 로그아웃
     *
     * @param request {@link HttpServletRequest} HTTP 요청 객체
     * @param response {@link HttpServletResponse} HTTP 응답 객체
     * @param authentication {@link Authentication} 인증 객체
     * @return {@link PostMessageResponseDto} 로그아웃 완료 문구
     */
    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<PostMessageResponseDto> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            userService.deleteRefreshToken();
            new SecurityContextLogoutHandler().logout(request, response, authentication);

            log.info("인증 객체의 삭제 확인: {}", SecurityContextHolder.getContext().getAuthentication() == null);
            return new ResponseEntity<>(new PostMessageResponseDto(PostMessage.LOGOUT_SUCCESS), HttpStatus.OK);
        }

        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "잘못된 접근입니다.");
    }

    /**
     * 토큰 갱신 (재로그인)
     * @param refreshTokenHeader 리프레시 토큰 정보가 담긴 Authorization 헤더
     * @return 새로 발급된 accessToken, refreshToken
     * @apiNote Refresh Token Rotation 방식
     * @see <a href="https://g-db.tistory.com/entry/Spring-Security-%EC%8A%A4%ED%94%84%EB%A7%81-%EB%B6%80%ED%8A%B8-Access-Token%EC%97%90%EC%84%9C-Refresh-Token%EC%B6%94%EA%B0%80%ED%95%98%EC%97%AC-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0">
     */
    @Operation(summary = "토큰 갱신(재로그인)")
    @PostMapping("/refresh-token")
    public ResponseEntity<JwtResponseDto> refreshToken(
            @RequestHeader("Authorization") String refreshTokenHeader
    ) {
        String refreshToken = refreshTokenHeader.replace("Bearer ", "");

        return new ResponseEntity<>(userService.updateToken(refreshToken), HttpStatus.OK);
    }
}
