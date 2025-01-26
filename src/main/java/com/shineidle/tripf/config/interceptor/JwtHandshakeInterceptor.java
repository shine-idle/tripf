package com.shineidle.tripf.config.interceptor;

import com.shineidle.tripf.common.exception.GlobalException;
import com.shineidle.tripf.common.exception.type.UserErrorCode;
import com.shineidle.tripf.common.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // Query Parameter에서 JWT 가져오기
        String query = request.getURI().getQuery();
        String token = null;

        if (query != null && query.contains("token=")) {
            token = query.split("token=")[1].split("&")[0]; // token= 뒤의 값을 추출
        }

        // JWT 유효성 검증
        if (token == null || !jwtProvider.validToken(token)) {
            log.error("Invalid or missing JWT token during WebSocket handshake");
            throw new GlobalException(UserErrorCode.TOKEN_NOT_FOUND);
        }

        // 유저 정보 저장
        String userEmail = jwtProvider.getUsername(token);
        attributes.put("userEmail", userEmail);

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        log.info("afterHandshake");
    }
}
