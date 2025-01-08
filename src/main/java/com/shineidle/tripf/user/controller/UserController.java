package com.shineidle.tripf.user.controller;

import com.shineidle.tripf.user.dto.UserRequestDto;
import com.shineidle.tripf.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> create(@RequestBody UserRequestDto dto) {
        return new ResponseEntity<>(userService.create(dto), HttpStatus.CREATED);
    }
}
