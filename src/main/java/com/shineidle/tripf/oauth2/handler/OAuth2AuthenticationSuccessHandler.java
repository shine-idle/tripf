package com.shineidle.tripf.oauth2.handler;

import com.shineidle.tripf.common.util.JwtProvider;
import com.shineidle.tripf.common.util.TokenType;
import com.shineidle.tripf.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.shineidle.tripf.oauth2.service.OAuth2UserPrincipal;
import com.shineidle.tripf.oauth2.user.OAuth2Provider;
import com.shineidle.tripf.oauth2.util.CookieUtils;
import com.shineidle.tripf.user.entity.RefreshToken;
import com.shineidle.tripf.user.entity.User;
import com.shineidle.tripf.user.repository.UserRepository;
import com.shineidle.tripf.user.service.RefreshTokenService;
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

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    public static final String REDIRECT_URL = "/";
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

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

        // 쿠키에 사용
        addRefreshTokenToCookie(request, response, refreshToken);

        //Path 뒤에 accessToken 붙이기
        String targetUrl = getTargetUrl(accessToken);

        clearAuthenticationAttributes(request, response);

        // 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String getTargetUrl(String accessToken) {
        return UriComponentsBuilder.fromUriString(REDIRECT_URL)
                .queryParam("accessToken", accessToken)
                .build().toUriString();
    }

    private OAuth2UserPrincipal getOAuth2UserPrincipal(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (principal instanceof OAuth2UserPrincipal) {
            return (OAuth2UserPrincipal) principal;
        }

        return null;
    }

    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, RefreshToken refreshToken) {
        int cookieMaxAge = jwtProvider.getRefreshExpiryMillis().intValue();
        CookieUtils.deleteCookie(request, response, "refresh_token");
        CookieUtils.addCookie(response, "refresh_token", refreshToken.getToken(), cookieMaxAge);
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }
}
