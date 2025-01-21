package com.shineidle.tripf.user.controller;

import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.user.dto.PasswordUpdateRequestDto;
import com.shineidle.tripf.user.dto.UserRequestDto;
import com.shineidle.tripf.user.dto.UserResponseDto;
import com.shineidle.tripf.user.dto.UsernameUpdateRequestDto;
import com.shineidle.tripf.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    /**
     * 권한 체크(비밀번호 체크)
     *
     * @param dto {@link UserRequestDto} 유저 요청 Dto
     * @return 프로필 페이지로 리다이렉트
     */
    @Operation(summary = "권한 체크(비밀번호 체크)")
    @PostMapping("/verify-authority")
    public ResponseEntity<Void> verify(
            @RequestBody UserRequestDto dto
    ) {
        userService.verify(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 유저 조회
     *
     * @param userId 유저 식별자
     * @return {@link UserResponseDto} 유저 응답 Dto
     */
    @Operation(summary = "유저 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> findUser(
            @PathVariable Long userId
    ) {
        return new ResponseEntity<>(userService.findUser(userId), HttpStatus.OK);
    }

    /**
     * 패스워드 수정
     *
     * @param dto {@link PasswordUpdateRequestDto} 패스워드 수정 요청 Dto
     * @return {@link PostMessageResponseDto} 수정 완료 문구
     */
    @Operation(summary = "패스워드 수정")
    @PatchMapping("/me")
    public ResponseEntity<PostMessageResponseDto> updatePassword(
            @RequestBody PasswordUpdateRequestDto dto
    ) {
        return new ResponseEntity<>(userService.updatePassword(dto), HttpStatus.OK);
    }

    /**
     * 유저 이름 수정
     *
     * @param dto {@link UsernameUpdateRequestDto} 유저 이름 수정 요청 Dto
     * @return {@link PostMessageResponseDto} 수정완료 문구
     */
    @Operation(summary = "유저이름 수정")
    @PutMapping("/me")
    public ResponseEntity<PostMessageResponseDto> updateName(
            @RequestBody UsernameUpdateRequestDto dto
    ) {
        return new ResponseEntity<>(userService.updateName(dto), HttpStatus.OK);
    }

    /**
     * 회원 탈퇴
     *
     * @param dto {@link UserRequestDto} 유저 요청 Dto
     * @return {@link PostMessageResponseDto} 탈퇴완료 문구
     */
    @Operation(summary = "회원탈퇴")
    @DeleteMapping("/deactivate")
    public ResponseEntity<PostMessageResponseDto> deleteUser(
            @RequestBody UserRequestDto dto
    ) {
        return new ResponseEntity<>(userService.deleteUser(dto), HttpStatus.OK);
    }
}
