package com.shineidle.tripf.global.security.oauth2.handler;

import com.shineidle.tripf.global.common.util.provider.JwtProvider;
import com.shineidle.tripf.domain.user.type.TokenType;
import com.shineidle.tripf.global.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.shineidle.tripf.global.security.oauth2.service.OAuth2UserPrincipal;
import com.shineidle.tripf.global.security.oauth2.user.OAuth2Provider;
import com.shineidle.tripf.global.security.oauth2.util.CookieUtils;
import com.shineidle.tripf.domain.user.entity.RefreshToken;
import com.shineidle.tripf.domain.user.entity.User;
import com.shineidle.tripf.domain.user.repository.UserRepository;
import com.shineidle.tripf.domain.user.service.RefreshTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

/**
 * OAuth2 인증 성공 시 실행되는 핸들러 클래스입니다.
 * 인증 성공 후 JWT 액세스 및 리프레시 토큰을 생성하고, 이를 쿠키에 저장한 후 리디렉션합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    public static final String REDIRECT_URL = "/";
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    /**
     * OAuth2 인증 성공 시 호출되는 메서드입니다.
     * 인증된 사용자의 정보를 기반으로 JWT 토큰을 생성하고 쿠키에 저장한 후 리디렉션합니다.
     *
     * @param request        HTTP 요청
     * @param response       HTTP 응답
     * @param authentication 인증 객체
     * @throws IOException 리디렉션 과정에서 발생할 수 있는 예외
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2UserPrincipal oAuth2UserPrincipal = getOAuth2UserPrincipal(authentication);

        User user = oAuth2UserPrincipal.getUserInfo().getProvider().equals(OAuth2Provider.KAKAO) ?
                userRepository.findByProviderId(oAuth2UserPrincipal.getUserInfo().getId()).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND)) :
                userRepository.findByEmail(oAuth2UserPrincipal.getName()).orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));

        String accessToken = jwtProvider.generateToken(authentication, true, TokenType.ACCESS);
        RefreshToken refreshToken = refreshTokenService.generateToken(user.getId(), authentication, true);

        addAccessTokenToCookie(request, response, accessToken);
        addRefreshTokenToCookie(request, response, refreshToken);

//        //Path 뒤에 accessToken 붙이기
//        String targetUrl = getTargetUrl(accessToken);

        clearAuthenticationAttributes(request, response);

        getRedirectStrategy().sendRedirect(request, response, REDIRECT_URL);
    }

    /**
     * 리디렉션할 URL에 액세스 토큰을 추가합니다.
     *
     * @param accessToken 액세스 토큰
     * @return 액세스 토큰이 추가된 URL
     */
    protected String getTargetUrl(String accessToken) {
        return UriComponentsBuilder.fromUriString(REDIRECT_URL)
                .queryParam("accessToken", accessToken)
                .build().toUriString();
    }

    /**
     * Authentication 객체에서 OAuth2UserPrincipal을 반환합니다.
     *
     * @param authentication 인증 객체
     * @return OAuth2UserPrincipal 객체 또는 null
     */
    private OAuth2UserPrincipal getOAuth2UserPrincipal(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2UserPrincipal) {
            return (OAuth2UserPrincipal) principal;
        }

        return null;
    }

    /**
     * 액세스 토큰을 쿠키에 추가합니다.
     *
     * @param request     HTTP 요청
     * @param response    HTTP 응답
     * @param accessToken 저장할 액세스 토큰
     */
    private void addAccessTokenToCookie(HttpServletRequest request, HttpServletResponse response, String accessToken) {
        int cookieMaxAge = jwtProvider.getRefreshExpiryMillis().intValue();
        CookieUtils.deleteCookie(request, response, "Authorization");
        CookieUtils.addCookie(response, "Authorization", accessToken, cookieMaxAge);
    }

    /**
     * 리프레시 토큰을 쿠키에 추가합니다.
     *
     * @param request      HTTP 요청
     * @param response     HTTP 응답
     * @param refreshToken 저장할 리프레시 토큰
     */
    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, RefreshToken refreshToken) {
        int cookieMaxAge = jwtProvider.getRefreshExpiryMillis().intValue();
        CookieUtils.deleteCookie(request, response, "refresh_token");
        CookieUtils.addCookie(response, "refresh_token", refreshToken.getToken(), cookieMaxAge);
    }

    /**
     * 인증 관련 속성을 제거하고, OAuth2 인증 요청 쿠키를 삭제합니다.
     *
     * @param request  HTTP 요청
     * @param response HTTP 응답
     */
    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}
