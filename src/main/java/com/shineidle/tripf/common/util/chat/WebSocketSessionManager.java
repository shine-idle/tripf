package com.shineidle.tripf.common.util.chat;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebSocketSessionManager {
    private final Map<String, String> sessionStore = new ConcurrentHashMap<>();
    private final Map<String, Long> sessionExpiration = new ConcurrentHashMap<>();
    private static final long EXPIRATION_TIME = 30 * 60 * 1000; // 30분

    public void addSession(String sessionId, String userEmail) {
        sessionStore.put(sessionId, userEmail);
        sessionExpiration.put(sessionId, System.currentTimeMillis() + EXPIRATION_TIME);
    }

    public void removeSession(String sessionId) {
        sessionStore.remove(sessionId);
        sessionExpiration.remove(sessionId);
    }

    public String getUserEmail(String sessionId) {
        return sessionStore.get(sessionId);
    }

    @Scheduled(fixedRate = 60000) // 1분마다 실행
    private void cleanExpiredSessions() {
        long currentTime = System.currentTimeMillis();
        sessionExpiration.forEach((sessionId, expirationTime) -> {
            if (expirationTime < currentTime) {
                removeSession(sessionId);
            }
        });
    }
}
