package com.shineidle.tripf.common.exception;

import com.shineidle.tripf.common.exception.type.CommonErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthenticationException.class)
    public void handleAuthenticationException(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String message = CommonErrorCode.INVALID_USER.getMessage();
        response.getWriter().write("{\"error\": \"Authentication required\", \"message\": \"" + message + "\"}");
    }

    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        String message;
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/api/admin")) {
            message = CommonErrorCode.NEED_ADMIN_ACCESS.getMessage();
        } else {
            message = CommonErrorCode.ACCESS_DENIED.getMessage();
        }

        response.getWriter().write("{\"error\": \"Access denied\", \"message\": \"" + message + "\"}");
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ExceptionResponseDto> CustomExceptionHandler(GlobalException e) {
        return new ResponseEntity<>(new ExceptionResponseDto(e.getErrCode(), e.getMessage()), e.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(fieldErrors);
    }
}