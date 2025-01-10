package com.shineidle.tripf.user.service;

import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.common.message.enums.PostMessage;
import com.shineidle.tripf.common.util.AuthenticationScheme;
import com.shineidle.tripf.common.util.JwtProvider;
import com.shineidle.tripf.user.dto.*;
import com.shineidle.tripf.user.entity.User;
import com.shineidle.tripf.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @Override
    @Transactional
    public PostMessageResponseDto create(UserRequestDto dto) {
        boolean duplicated = userRepository.findByEmail(dto.getEmail()).isPresent();
        if (duplicated) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "중복된 이메일 입니다.");
        }

        User user = new User(dto.getEmail(),
                passwordEncoder.encode(dto.getPassword()),
                dto.getName(),
                dto.getAuth(),
                dto.getAddress()
        );

        userRepository.save(user);

        return new PostMessageResponseDto(PostMessage.SIGNUP_SUCCESS);
    }

    @Override
    public JwtResponseDto login(UserRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));

        validatePassword(dto.getPassword(), user.getPassword());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 토큰 생성
        String accessToken = jwtProvider.generateToken(authentication);
        log.info("토큰: {}", accessToken);

        return new JwtResponseDto(AuthenticationScheme.BEARER.getName(), accessToken);
    }

    @Override
    public UserResponseDto find(Long userId) {
        User findUser = getUserById(userId);
        return UserResponseDto.toDto(findUser);
    }

    @Override
    @Transactional
    public PostMessageResponseDto updatePassword(Long userId, PasswordUpdateRequestDto dto) {
        User findUser = getUserById(userId);
        validatePassword(dto.getPassword(), findUser.getPassword());
        findUser.updatePassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(findUser);

        return new PostMessageResponseDto(PostMessage.PASSWORD_UPDATED);
    }

    @Override
    @Transactional
    public PostMessageResponseDto updateName(Long userId, UsernameUpdateRequestDto dto) {
        User findUser = getUserById(userId);
        findUser.updateName(dto.getName());
        userRepository.save(findUser);

        //로그 아웃

        return new PostMessageResponseDto(PostMessage.USERNAME_UPDATED);
    }


    private User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."));
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }
    }
}
