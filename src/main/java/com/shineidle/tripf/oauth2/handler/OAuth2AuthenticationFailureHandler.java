package com.shineidle.tripf.oauth2.handler;

import com.shineidle.tripf.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.shineidle.tripf.oauth2.util.CookieUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

import static com.shineidle.tripf.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME;

/**
 * OAuth2 인증 실패 시 실행되는 핸들러 클래스입니다.
 * 인증 실패 시 특정 URL로 리디렉션하며, 에러 메시지를 쿼리 파라미터로 추가합니다.
 */
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    /**
     * OAuth2 인증 실패 시 호출되는 메서드입니다.
     * 인증 요청에 사용된 쿠키를 삭제하고, 실패 메시지를 포함하여 클라이언트를 리디렉션합니다.
     *
     * @param request   HTTP 요청
     * @param response  HTTP 응답
     * @param exception 인증 예외
     * @throws IOException 리디렉션 과정에서 발생할 수 있는 예외
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {
        String targetUrl = CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse(("/"));

        targetUrl = UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("error", exception.getLocalizedMessage())
                .build().toString();

        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}