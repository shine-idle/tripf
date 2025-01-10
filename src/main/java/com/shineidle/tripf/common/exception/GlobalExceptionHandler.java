package com.shineidle.tripf.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
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

        String requestURI = request.getRequestURI();
        String message;
        if (requestURI.startsWith("/api/admin")) {
            message = "관리자 권한이 필요합니다.";
        } else {
            message = "로그인이 필요합니다.";
        }

        response.getWriter().write("{\"error\": \"Authentication required\", \"message\": \"" + message + "\"}");
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

//    @ExceptionHandler(ResponseStatusException.class)
//    public ResponseEntity<Map<String, String>> handleResponseStatusException(ResponseStatusException e) {
//
//        Map<String, String> response = new HashMap<>();
//        response.put("error code", e.getStatusCode().toString());
//        response.put("message", e.getReason());
//
//        return new ResponseEntity<>(response, e.getStatusCode());
//    }
}