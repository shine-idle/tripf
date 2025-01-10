package com.shineidle.tripf.user.controller;

import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.user.dto.PasswordUpdateRequestDto;
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

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> find(
            @PathVariable Long userId
    ) {
        return new ResponseEntity<>(userService.find(userId), HttpStatus.OK);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<PostMessageResponseDto> updatePassword(
            @PathVariable Long userId,
            @RequestBody PasswordUpdateRequestDto dto
    ) {
        return new ResponseEntity<>(userService.updatePassword(userId, dto), HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<PostMessageResponseDto> updateName(
            @PathVariable Long userId,
            @RequestBody UsernameUpdateRequestDto dto
    ) {
        return new ResponseEntity<>(userService.updateName(userId, dto), HttpStatus.OK);
    }
}
