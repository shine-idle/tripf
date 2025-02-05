package com.shineidle.tripf.global.common.util.chat;

import com.shineidle.tripf.global.common.exception.GlobalException;
import com.shineidle.tripf.global.common.exception.type.CommonErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {
    private final WebSocketSessionManager sessionManager;

    /**
     * STOMP 메시지를 전송하기 전에 실행되는 메서드</br>
     * 연결(Connect) 및 연결 해제(Disconnect) 이벤트를 감지하여 처리
     *
     * @param message 전송될 메시지 객체
     * @param channel 메시지가 전송될 채널
     * @return 처리된 메시지 객체
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (accessor.getCommand() != null) {
            switch (accessor.getCommand()) {
                case CONNECT:
                    handleConnect(accessor);
                    break;
                case DISCONNECT:
                    handleDisconnect(accessor);
                    break;
                default:
                    break;
            }
        }

        return message;
    }

    /**
     * STOMP 메시지가 전송된 후 실행되는 메서드</br>
     * 메시지의 명령어와 헤더 정보를 로깅
     *
     * @param message 전송된 메시지 객체
     * @param channel 메시지가 전송된 채널
     * @param sent    메시지가 성공적으로 전송되었는지 여부
     */
    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (accessor.getCommand() != null) {
            log.info("Command (postSend): {}", accessor.getCommand());
            log.info("Headers (postSend): {}", accessor.toNativeHeaderMap());
        }
    }

    /**
     * 클라이언트가 WebSocket에 연결할 때 호출</br>
     * 세션 Id를 세션 매니저에 등록
     *
     * @param accessor STOMP 헤더 액세서 객체
     */
    private void handleConnect(StompHeaderAccessor accessor) {
        String sessionId = accessor.getSessionId();

        if (sessionId == null) {
            throw new GlobalException(CommonErrorCode.ACCESS_DENIED);
        }

        sessionManager.addSession(sessionId, "CONNECTED_USER");
    }

    /**
     * 클라이언트가 WebSocket 연결을 해제할 때 호출</br>
     * 세션 Id를 기반으로 세션을 제거
     *
     * @param accessor STOMP 헤더 액세서 객체
     */
    private void handleDisconnect(StompHeaderAccessor accessor) {
        String sessionId = accessor.getSessionId();

        if (sessionId != null) {

            sessionManager.removeSession(sessionId);
        }
    }
}
