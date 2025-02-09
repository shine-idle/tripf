package com.shineidle.tripf.global.security.oauth2.util;

import com.shineidle.tripf.global.common.exception.GlobalException;
import com.shineidle.tripf.global.common.exception.type.CommonErrorCode;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

public class CookieUtils {

    /**
     * 요청에서 특정 이름의 쿠키를 가져옵니다.
     *
     * @param request HTTP 요청 객체
     * @param name    가져올 쿠키의 이름
     * @return 해당 쿠키를 포함하는 Optional 객체, 없으면 Optional.empty()
     */
    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * 응답에 새로운 쿠키를 추가합니다.
     *
     * @param response HTTP 응답 객체
     * @param name     쿠키 이름
     * @param value    쿠키 값
     * @param maxAge   쿠키의 최대 유효 시간 (초 단위)
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    /**
     * 요청과 응답에서 특정 이름의 쿠키를 삭제합니다.
     *
     * @param request  HTTP 요청 객체
     * @param response HTTP 응답 객체
     * @param name     삭제할 쿠키의 이름
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    /**
     * 객체를 직렬화하여 Base64 문자열로 변환합니다.
     *
     * @param object 직렬화할 객체
     * @return 직렬화된 Base64 문자열
     */
    public static String serialize(Object object) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(object));
    }

    /**
     * Base64 문자열을 역직렬화하여 객체로 변환합니다.
     *
     * @param cookie 역직렬화할 쿠키 객체
     * @param tClass 변환할 클래스 타입
     * @param <T>    클래스 타입 제네릭
     * @return 역직렬화된 객체
     * @throws GlobalException      쿠키 값이 null이거나 비어있을 경우 예외 발생
     * @throws NullPointerException 쿠키가 null일 경우 예외 발생
     */
    public static <T> T deserialize(Cookie cookie, Class<T> tClass) {
        Objects.requireNonNull(cookie, CommonErrorCode.COOKIE_NOT_FOUND.getMessage());
        String cookieValue = cookie.getValue();

        if (cookieValue == null || cookieValue.isEmpty()) {
            throw new GlobalException(CommonErrorCode.COOKIE_NOT_FOUND);
        }

        byte[] decodedBytes = Base64.getUrlDecoder().decode(cookieValue);
        return tClass.cast(org.apache.commons.lang3.SerializationUtils.deserialize(decodedBytes));
    }
}
