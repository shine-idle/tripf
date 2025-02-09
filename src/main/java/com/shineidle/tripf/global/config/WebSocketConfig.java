package com.shineidle.tripf.global.config;

import com.shineidle.tripf.global.common.util.chat.StompHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket 설정 클래스
 * <p>
 * STOMP 프로토콜을 기반으로 웹소켓 통신을 설정,
 * 메시지 브로커 및 엔드포인트를 구성
 * </p>
 */
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final StompHandler stompHandler;

    /**
     * 메시지 브로커를 구성하는 메서드
     * <p>
     * "topic" 으로 시작하는 경로를 구독한 클라이언트에게 메시지를 브로커가 전달한다.
     * 클라이언트가 메시지를 보낼 때 "/app" 접두사를 붙여야 한다.
     * </p>
     *
     * @param config 메시지 브로커 레지스트리 객체
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // 브로커를 통해 클라이언트로 메시지 전송
        config.setApplicationDestinationPrefixes("/app"); // 클라이언트가 서버로 메시지 전할 때의 경로 (@MessageMapping 메서드로 매핑됨)
    }

    /**
     * STOMP 엔드포인트를 등록하는 메서드
     * <p>
     * 클라이언트가 웹소켓을 연결할 때 사용할 엔드포인트를 설정한다.
     * 모든 도메인에서의 접근을 허용하도록 CORS 설정을 적용한다.
     * </p>
     *
     * @param registry STOMP 엔드포인트 레지스트리 객체
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/gs-guide-websocket") // 웹소켓 연결 엔드포인트
                .setAllowedOriginPatterns("*"); // CORS 설정
    }

    /**
     * 클라이언트에서 들어오는 메시지를 가로채는 인터셉터를 등록하는 메서드
     * <p>
     * StompHandler를 이용해 연결 및 인증을 처리할 수 있도록 한다.
     * </p>
     *
     * @param registration 채널 등록 객체
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }
}
