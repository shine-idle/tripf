package com.shineidle.tripf.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthenticationException.class)
    public void handleAuthenticationException(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String requestURI = request.getRequestURI();
        String message;
        if (requestURI.startsWith("/api/admin")) {
            message = "관리자 권한이 필요합니다.";
        } else {
            message = "로그인이 필요합니다.";
        }

        response.getWriter().write("{\"error\": \"Authentication required\", \"message\": \"" + message + "\"}");
    }
}
