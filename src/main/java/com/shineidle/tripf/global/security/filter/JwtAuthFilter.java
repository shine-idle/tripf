package com.shineidle.tripf.global.security.filter;

import com.shineidle.tripf.global.common.util.provider.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        this.authenticate(request);
        filterChain.doFilter(request, response);
    }

    /**
     * 인증을 처리
     *
     * @param request {@link HttpServletRequest}
     */
    private void authenticate(HttpServletRequest request) {
        String token = this.getTokenFromRequest(request);

        if (jwtProvider.isInvalidToken(token)) {
            //TODO : 토큰 만료 -> 프론트에서 다시 로그인 하도록 유도?
            return;
        }

        String username = this.jwtProvider.getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        this.setAuthentication(request, userDetails);
    }

    /**
     * Authorization 헤더에서 토큰 값을 가져오기
     *
     * @param request {@link HttpServletRequest}
     * @return 토큰 (찾지 못한 경우 null)
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String token = getTokenFromHeader(request);
        if (token == null) {
            token = getTokenFromCookie(request);
        }

        return token;
    }

    private String getTokenFromHeader(HttpServletRequest request) {
        final String accessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(accessToken)) {
            return accessToken.replace("Bearer ", "");
        }

        return null;
    }

    private String getTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> "Authorization".equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    /**
     * SecurityContext에 인증 객체를 저장
     *
     * @param request     {@link HttpServletRequest}
     * @param userDetails 찾아온 유저 정보
     */
    private void setAuthentication(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }
}
