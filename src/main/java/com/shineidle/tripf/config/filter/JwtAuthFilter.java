package com.shineidle.tripf.config.filter;

import com.shineidle.tripf.common.util.AuthenticationScheme;
import com.shineidle.tripf.common.util.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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

    private void authenticate(HttpServletRequest request) {
        String token = this.getTokenFromRequest(request);
        if (!jwtProvider.validToken(token)) {
            return;
        }

        String username = this.jwtProvider.getUsername(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        this.setAuthentication(request, userDetails);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String headerPrefix = AuthenticationScheme.generateType(AuthenticationScheme.BEARER);

        boolean tokenExist = StringUtils.hasText(bearerToken) && bearerToken.startsWith(headerPrefix);
        if (tokenExist) {
            return bearerToken.substring(headerPrefix.length());
        }

        return null;
    }

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
