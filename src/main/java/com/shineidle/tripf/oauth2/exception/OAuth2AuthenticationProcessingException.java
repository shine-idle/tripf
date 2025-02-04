package com.shineidle.tripf.oauth2.exception;

import org.springframework.security.core.AuthenticationException;

// TODO : javadoc 작성
public class OAuth2AuthenticationProcessingException extends AuthenticationException {
    public OAuth2AuthenticationProcessingException(String message) {
        super(message);
    }
}
