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

    @PostMapping("/verify-authority")
    public ResponseEntity<Void> verify(
            @RequestBody UserRequestDto dto
    ) {
        userService.verify(dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> findUser(
            @PathVariable Long userId
    ) {
        return new ResponseEntity<>(userService.findUser(userId), HttpStatus.OK);
    }

    @PatchMapping("/me")
    public ResponseEntity<PostMessageResponseDto> updatePassword(
            @RequestBody PasswordUpdateRequestDto dto
    ) {
        return new ResponseEntity<>(userService.updatePassword(dto), HttpStatus.OK);
    }

    @PutMapping("/me")
    public ResponseEntity<PostMessageResponseDto> updateName(
            @RequestBody UsernameUpdateRequestDto dto
    ) {
        return new ResponseEntity<>(userService.updateName(dto), HttpStatus.OK);
    }

    @PatchMapping("/deactivate")
    public ResponseEntity<PostMessageResponseDto> deleteUser(
            @RequestBody UserRequestDto dto
    ) {
        return new ResponseEntity<>(userService.deleteUser(dto), HttpStatus.OK);
    }
}
