package com.shineidle.tripf.user.service;

import com.shineidle.tripf.common.exception.GlobalException;
import com.shineidle.tripf.common.exception.type.UserErrorCode;
import com.shineidle.tripf.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.common.message.type.PostMessage;
import com.shineidle.tripf.common.util.*;
import com.shineidle.tripf.common.util.auth.AuthenticationScheme;
import com.shineidle.tripf.common.util.auth.UserAuthorizationUtil;
import com.shineidle.tripf.common.util.redis.RedisUtils;
import com.shineidle.tripf.oauth2.util.CookieUtils;
import com.shineidle.tripf.user.dto.*;
import com.shineidle.tripf.user.entity.RefreshToken;
import com.shineidle.tripf.user.entity.User;
import com.shineidle.tripf.user.repository.UserRepository;
import com.shineidle.tripf.user.type.TokenType;
import com.shineidle.tripf.user.type.UserStatus;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final RedisUtils redisUtils;

    /**
     * 유저 생성
     *
     * @param dto {@link UserRequestDto} <br>
     * email(이메일,String), password(비밀번호, String), name(이름, String), address(주소, String), auth(권한, UserAuth)
     * @return {@link PostMessageResponseDto} 회원가입 완료 문구
     */
    @Override
    @Transactional
    public PostMessageResponseDto createUser(UserRequestDto dto) {
        Optional<User> existUser = userRepository.findByEmail(dto.getEmail());

        if (existUser.isPresent() && existUser.get().getStatus().equals(UserStatus.DEACTIVATE)) {
            throw new GlobalException(UserErrorCode.USER_DEACTIVATED);
        }

        if (existUser.isPresent()) {
            throw new GlobalException(UserErrorCode.EMAIL_DUPLICATED);
        }

        User user = new User(
                dto.getEmail(),
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
     *
     * @param dto {@link UserRequestDto} <br>
     * email(이메일,String), password(비밀번호, String)
     * @return {@link JwtResponseDto}
     * @apiNote 최초 로그인 시 엑세스토큰과 리프레시 토큰을 발급
     */
    @Override
    public JwtResponseDto login(UserRequestDto dto, HttpServletResponse response) {
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(() ->
                new GlobalException(UserErrorCode.USER_NOT_FOUND));

        // 탈퇴된 회원에 대한 예외처리
        if (user.getStatus().equals(UserStatus.DEACTIVATE)) {
            throw new GlobalException(UserErrorCode.USER_DEACTIVATED);
        }

        validatePassword(dto.getPassword(), user.getPassword());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 토큰 생성
        String accessToken = jwtProvider.generateToken(authentication, false, TokenType.ACCESS);
        RefreshToken refreshToken = refreshTokenService.generateToken(user.getId(), authentication, false);

        // 쿠키 저장
        CookieUtils.addCookie(response, "Authorization", accessToken, jwtProvider.getAccessExpiryMillis().intValue());
        CookieUtils.addCookie(response, "refresh_token", refreshToken.getToken(), jwtProvider.getRefreshExpiryMillis().intValue());

        return new JwtResponseDto(AuthenticationScheme.BEARER.getName(), accessToken, refreshToken.getToken());
    }

    /**
     * 토큰 갱신(재로그인)
     *
     * @param refreshToken {@link RefreshToken}
     * @return {@link JwtResponseDto} 갱신된 토큰 정보
     */
    @Override
    public JwtResponseDto updateToken(String refreshToken) {
        RefreshToken validRefreshToken = refreshTokenService.findByToken(refreshToken).orElseThrow(() ->
                new GlobalException(UserErrorCode.TOKEN_NOT_FOUND));

        // 유효한 토큰인지 확인
        refreshTokenService.verifyExpiration(validRefreshToken);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String accessToken = jwtProvider.generateToken(authentication, false, TokenType.ACCESS);
        RefreshToken newRefreshToken = refreshTokenService.rotateRefreshToken(validRefreshToken, authentication);

        return new JwtResponseDto(AuthenticationScheme.BEARER.getName(), accessToken, newRefreshToken.getToken());
    }

    /**
     * 유저 조회
     *
     * @param userId 유저 식별자
     * @return {@link UserResponseDto} 유저 응답 Dto
     */
    @Override
    public UserResponseDto findUser(Long userId) {
        User user = (User) redisUtils.getFromRedis("USER:" + userId.toString());
        if (user == null) {
            user = getUserById(userId);
            redisUtils.saveToRedis("USER:" + user.getId().toString(), user, Duration.ofMinutes(1));
        }
        return UserResponseDto.toDto(user);
    }

    /**
     * 비밀번호 변경
     *
     * @param dto {@link PasswordUpdateRequestDto} <br>
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

        refreshTokenService.deleteToken(user.getId());

        return new PostMessageResponseDto(PostMessage.PASSWORD_UPDATED);
    }

    /**
     * 이름 수정
     *
     * @param dto {@link UsernameUpdateRequestDto} <br>
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
     *
     * @param dto {@link UserRequestDto} <br>
     * password(비밀번호, String)
     * @return {@link PostMessageResponseDto} 탈퇴처리 문구
     */
    @Override
    public PostMessageResponseDto deleteUser(UserRequestDto dto) {
        User user = UserAuthorizationUtil.getLoginUser();
        validatePassword(dto.getPassword(), user.getPassword());

        user.deactivate();
        userRepository.save(user);

        // 리프레시 토큰 삭제
        refreshTokenService.deleteToken(user.getId());

        return new PostMessageResponseDto(PostMessage.USER_DEACTIVATED);
    }

    /**
     * 비밀번호 확인
     *
     * @param dto {@link UserRequestDto} 유저 요청 Dto (password)
     */
    @Override
    public void verify(UserRequestDto dto) {
        validatePassword(dto.getPassword(), UserAuthorizationUtil.getLoginUserPassword());
    }

    public void deleteRefreshToken() {
        refreshTokenService.deleteToken(UserAuthorizationUtil.getLoginUserId());
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

    /**
     * 활성상태의 유저 Email 반환
     * @return getActiveUserEmails
     */
    @Override
    public List<String> getActiveUserEmails() {
        return userRepository.findAllActiveEmails();
    }

}
