package com.shineidle.tripf.domain.user.service;

import com.shineidle.tripf.global.common.message.dto.PostMessageResponseDto;
import com.shineidle.tripf.domain.user.dto.*;
import com.shineidle.tripf.domain.user.entity.User;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface UserService {
    /**
     * 유저 생성
     *
     * @param dto 유저 생성 요청 데이터
     * @return 유저 생성 결과 메시지
     */
    PostMessageResponseDto createUser(UserRequestDto dto);

    /**
     * 로그인
     *
     * @param dto      로그인 요청 데이터
     * @param response HTTP 응답 객체 (쿠키에 토큰 정보 전달)
     * @return JWT 응답 데이터 (토큰 정보)
     */
    JwtResponseDto login(UserRequestDto dto, HttpServletResponse response);

    /**
     * 특정 유저 조회
     *
     * @param userId 유저 Id
     * @return 특정 유저 정보 응답 데이터
     */
    UserResponseDto findUser(Long userId);

    /**
     * 비밀번호 변경
     *
     * @param dto 비밀번호 변경 요청 데이터
     * @return 비밀번호 변경 확인 메시지
     */
    PostMessageResponseDto updatePassword(PasswordUpdateRequestDto dto);

    /**
     * 이름 변경
     *
     * @param dto 이름 변경 요청 데이터
     * @return 이름 변경 확인 메시지
     */
    PostMessageResponseDto updateName(UsernameUpdateRequestDto dto);

    /**
     * 유저 삭제
     *
     * @param dto 유저 삭제 요청 데이터 (비밀번호)
     * @return 유저 삭제 확인 메시지
     */
    PostMessageResponseDto deleteUser(UserRequestDto dto);

    /**
     * 유저 인증을 검증
     *
     * @param dto 사용자 인증 요청 데이터 (비밀번호)
     */
    void verify(UserRequestDto dto);

    /**
     * 리프레시 토큰을 이용해 새로운 토큰 발급 (액세스 토큰, 리프레시 토큰)
     *
     * @param refreshToken 리프레시 토큰 값
     * @return 토큰 정보
     */
    JwtResponseDto updateToken(String refreshToken);

    /**
     * 리프레시 토큰 삭제
     */
    void deleteRefreshToken();

    /**
     * Id로 유저 조회
     *
     * @param id 유저 Id
     * @return User 객체
     */
    User getUserById(Long id);

    /**
     * 활성화된 유저의 이메일 리스트 반환
     *
     * @return 이메일 리스트
     */
    List<String> getActiveUserEmails();
}
