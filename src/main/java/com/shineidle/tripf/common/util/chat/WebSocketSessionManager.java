package com.shineidle.tripf.common.util.chat;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket 세션을 관리하는 클래스</br>
 * 사용자 세션을 저장하고, 일정 시간 후 만료되는 기능을 제공
 */
@Service
public class WebSocketSessionManager {
    private final Map<String, String> sessionStore = new ConcurrentHashMap<>();
    private final Map<String, Long> sessionExpiration = new ConcurrentHashMap<>();
    private static final long EXPIRATION_TIME = 30 * 60 * 1000; // 30분

    /**
     * 새로운 세션을 추가하고 만료 시간을 설정
     *
     * @param sessionId 세션 Id
     * @param userEmail 사용자 이메일
     */
    public void addSession(String sessionId, String userEmail) {
        sessionStore.put(sessionId, userEmail);
        sessionExpiration.put(sessionId, System.currentTimeMillis() + EXPIRATION_TIME);
    }

    /**
     * 특정 세션을 제거
     *
     * @param sessionId 제거할 세션 Id
     */
    public void removeSession(String sessionId) {
        sessionStore.remove(sessionId);
        sessionExpiration.remove(sessionId);
    }

    /**
     * 세션 Id를 이용해 사용자 이메일을 반환
     *
     * @param sessionId 조회할 세션 Id
     * @return 사용자 이메일, 없을 경우 null 반환
     */
    public String getUserEmail(String sessionId) {
        return sessionStore.get(sessionId);
    }

    /**
     * 일정 시간마다 실행되며, 만료된 세션을 정리
     */
    @Scheduled(fixedRate = 60000)
    private void cleanExpiredSessions() {
        long currentTime = System.currentTimeMillis();
        sessionExpiration.forEach((sessionId, expirationTime) -> {
            if (expirationTime < currentTime) {
                removeSession(sessionId);
            }
        });
    }
}
