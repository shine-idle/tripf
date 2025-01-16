package com.shineidle.tripf.user.service;

import com.shineidle.tripf.common.exception.GlobalException;
import com.shineidle.tripf.common.exception.type.UserErrorCode;
import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.common.message.enums.PostMessage;
import com.shineidle.tripf.common.util.AuthenticationScheme;
import com.shineidle.tripf.common.util.JwtProvider;
import com.shineidle.tripf.common.util.UserAuthorizationUtil;
import com.shineidle.tripf.user.dto.*;
import com.shineidle.tripf.user.entity.User;
import com.shineidle.tripf.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    /**
     * 유저 생성
     * @param dto {@link UserRequestDto} </br>
     * email(이메일,String), password(비밀번호, String), name(이름, String), address(주소, String), auth(권한, UserAuth)
     * @return {@link PostMessageResponseDto} 회원가입 완료 문구
     */
    @Override
    @Transactional
    public PostMessageResponseDto createUser(UserRequestDto dto) {
        boolean duplicated = userRepository.findByEmail(dto.getEmail()).isPresent();
        if (duplicated) {
            throw new GlobalException(UserErrorCode.EMAIL_DUPLICATED);
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

    /**
     * 로그인
     * @param dto {@link UserRequestDto} </br>
     * email(이메일,String), password(비밀번호, String)
     * @return {@link JwtResponseDto}
     */
    @Override
    public JwtResponseDto login(UserRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() ->
                new GlobalException(UserErrorCode.USER_NOT_FOUND));

        validatePassword(dto.getPassword(), user.getPassword());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 토큰 생성
        String accessToken = jwtProvider.generateToken(authentication, false);
        log.info("토큰: {}", accessToken);
        log.info("로그인 유저의 Id: {}", UserAuthorizationUtil.getLoginUserId());
        log.info(UserAuthorizationUtil.getLoginUserEmail());
        log.info(UserAuthorizationUtil.getLoginUserAuthority().toString());

        return new JwtResponseDto(AuthenticationScheme.BEARER.getName(), accessToken);
    }

    /**
     * 유저 조회
     * @param userId 유저Id
     * @return {@link UserResponseDto}
     */
    @Override
    public UserResponseDto findUser(Long userId) {
        User findUser = getUserById(userId);
        return UserResponseDto.toDto(findUser);
    }

    /**
     * 비밀번호 변경
     * @param dto {@link PasswordUpdateRequestDto} </br>
     * password(비밀번호, String), newPassword(새 비민번호, String)
     * @return {@link PostMessageResponseDto} 비밀번호 수정완료 문구
     */
    @Override
    @Transactional
    public PostMessageResponseDto updatePassword(PasswordUpdateRequestDto dto) {
        User user = UserAuthorizationUtil.getLoginUser();
        validatePassword(dto.getPassword(), user.getPassword());
        user.updatePassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);

        // logout

        return new PostMessageResponseDto(PostMessage.PASSWORD_UPDATED);
    }

    /**
     * 이름 수정
     * @param dto {@link UsernameUpdateRequestDto} </br>
     * name(이름, String)
     * @return {@link PostMessageResponseDto} 이름 수정완료 문구
     */
    @Override
    @Transactional
    public PostMessageResponseDto updateName(UsernameUpdateRequestDto dto) {
        User user = UserAuthorizationUtil.getLoginUser();
        user.updateName(dto.getName());
        userRepository.save(user);
        return new PostMessageResponseDto(PostMessage.USERNAME_UPDATED);
    }

    /**
     * 유저 탈퇴 처리
     * @param dto {@link UserRequestDto} </br>
     * password(비밀번호, String)
     * @return {@link PostMessageResponseDto} 탈퇴처리 문구
     */
    @Override
    public PostMessageResponseDto deleteUser(UserRequestDto dto) {
        User user = UserAuthorizationUtil.getLoginUser();
        validatePassword(dto.getPassword(), user.getPassword());

        user.deactivate();
        userRepository.save(user);

        // logout

        return new PostMessageResponseDto(PostMessage.USER_DEACTIVATED);
    }

    @Override
    public void verify(UserRequestDto dto) {
        validatePassword(dto.getPassword(), UserAuthorizationUtil.getLoginUserPassword());
    }


    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new GlobalException(UserErrorCode.USER_NOT_FOUND));
    }

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new GlobalException(UserErrorCode.PASSWORD_MISMATCH);
        }
    }
}
