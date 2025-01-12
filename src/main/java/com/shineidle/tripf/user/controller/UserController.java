package com.shineidle.tripf.user.controller;

import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.user.dto.PasswordUpdateRequestDto;
import com.shineidle.tripf.user.dto.UserRequestDto;
import com.shineidle.tripf.user.dto.UserResponseDto;
import com.shineidle.tripf.user.dto.UsernameUpdateRequestDto;
import com.shineidle.tripf.user.service.UserService;
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
     * @param dto {@link UserRequestDto}
     * @return 프로필 페이지로 리다이렉트
     */
    @PostMapping("/verify-authority")
    public ResponseEntity<Void> verify(
            @RequestBody UserRequestDto dto
    ) {
        userService.verify(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 유저 조회
     * @param userId 유저Id
     * @return {@link UserResponseDto}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> findUser(
            @PathVariable Long userId
    ) {
        return new ResponseEntity<>(userService.findUser(userId), HttpStatus.OK);
    }

    /**
     * 패스워드 수정
     * @param dto {@link PasswordUpdateRequestDto}
     * @return {@link PostMessageResponseDto} 수정완료 문구
     */
    @PatchMapping("/me")
    public ResponseEntity<PostMessageResponseDto> updatePassword(
            @RequestBody PasswordUpdateRequestDto dto
    ) {
        return new ResponseEntity<>(userService.updatePassword(dto), HttpStatus.OK);
    }

    /**
     * 이름 수정
     * @param dto {@link UsernameUpdateRequestDto}
     * @return {@link PostMessageResponseDto} 수정완료 문구
     */
    @PutMapping("/me")
    public ResponseEntity<PostMessageResponseDto> updateName(
            @RequestBody UsernameUpdateRequestDto dto
    ) {
        return new ResponseEntity<>(userService.updateName(dto), HttpStatus.OK);
    }

    /**
     * 회원 탈퇴
     * @param dto {@link UserRequestDto}
     * @return {@link PostMessageResponseDto} 탈퇴완료 문구
     */
    @PatchMapping("/deactivate")
    public ResponseEntity<PostMessageResponseDto> deleteUser(
            @RequestBody UserRequestDto dto
    ) {
        return new ResponseEntity<>(userService.deleteUser(dto), HttpStatus.OK);
    }
}
