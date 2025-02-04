package com.shineidle.tripf.oauth2.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * OAuth2 인증 과정에서 발생하는 예외를 처리하는 클래스입니다.
 */
public class OAuth2AuthenticationProcessingException extends AuthenticationException {
    /**
     * 예외 메시지를 포함하여 OAuth2 인증 예외를 생성합니다.
     *
     * @param message 예외 메시지
     */
    public OAuth2AuthenticationProcessingException(String message) {
        super(message);
    }
}
