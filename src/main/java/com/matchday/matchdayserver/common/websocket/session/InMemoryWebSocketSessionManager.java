package com.matchday.matchdayserver.common.websocket.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.WebSocketStatus;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 메모리 기반 웹소켓 세션 관리 구현체
 * 추후 Redis 등으로 확장 가능
 */
@Slf4j
@Component
public class InMemoryWebSocketSessionManager implements WebSocketSessionManager {

    // sessionId -> userId 매핑
    private final ConcurrentHashMap<String, Long> sessionToUserMap = new ConcurrentHashMap<>();

    // userId -> sessionId 매핑 (단일 세션 가정)
    private final ConcurrentHashMap<Long, String> userToSessionMap = new ConcurrentHashMap<>();

    @Override
    public void putSession(String sessionId, Long userId) {
        log.info("Storing session mapping: sessionId={}, userId={}", sessionId, userId);

        // 기존 세션이 있다면 제거
        removeUserSessions(userId);

        sessionToUserMap.put(sessionId, userId);
        userToSessionMap.put(userId, sessionId);
    }

    @Override
    public Optional<Long> getUserId(String sessionId) {
        Long userId = sessionToUserMap.get(sessionId);
        if (userId == null) {
            log.warn("No userId found for sessionId={}", sessionId);
            throw new ApiException(WebSocketStatus.SESSION_NOT_FOUND);
        }
        log.debug("Retrieved userId={} for sessionId={}", userId, sessionId);
        return Optional.ofNullable(userId);
    }

    @Override
    public void removeSession(String sessionId) {
        log.info("Removing session: sessionId={}", sessionId);

        Long userId = sessionToUserMap.remove(sessionId);
        if (userId != null) {
            userToSessionMap.remove(userId);
            log.info("Removed session mapping: sessionId={}, userId={}", sessionId, userId);
        }
    }

    @Override
    public void removeUserSessions(Long userId) {
        String existingSessionId = userToSessionMap.remove(userId);
        if (existingSessionId != null) {
            sessionToUserMap.remove(existingSessionId);
            log.info("Removed existing session for userId={}, sessionId={}", userId, existingSessionId);
        }
    }

    @Override
    public boolean existsSession(String sessionId) {
        boolean exists = Optional.ofNullable(sessionToUserMap.get(sessionId)).isPresent();
        log.debug("Session exists for sessionId={}: {}", sessionId, exists);
        return exists;
    }

}
