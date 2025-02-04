package com.shineidle.tripf.config;

import com.shineidle.tripf.common.util.chat.StompHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * TODO : javadoc
 */
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // 브로커를 통해 클라이언트로 메시지 전송
        config.setApplicationDestinationPrefixes("/app"); // 클라이언트가 서버로 메시지 전할 때의 경로 (@MessageMapping 메서드로 매핑됨)
    }

//    @Override
//    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
//        return messageConverters.add(new MappingJackson2MessageConverter());
//    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/gs-guide-websocket") // 웹소켓 연결 엔드포인트
                .setAllowedOriginPatterns("*"); // CORS 설정
//        registry.addEndpoint("/ws-stomp")
//                .setAllowedOrigins("*")
//                .withSockJS()
//                .setInterceptors(new JwtHandshakeInterceptor());
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }
}
