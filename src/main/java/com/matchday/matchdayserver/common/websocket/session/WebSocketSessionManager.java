package com.matchday.matchdayserver.common.websocket.session;

import java.util.Optional;

/**
 * 웹소켓 세션 관리를 위한 인터페이스
 * DIP를 통해 Redis 등 다른 저장소로 쉽게 확장 가능
 */
public interface WebSocketSessionManager {

    /**
     * 세션 ID와 사용자 ID 매핑 저장
     * 
     * @param sessionId 웹소켓 세션 ID
     * @param userId    사용자 ID
     */
    void putSession(String sessionId, Long userId);

    /**
     * 세션 ID로 사용자 ID 조회
     * 
     * @param sessionId 웹소켓 세션 ID
     * @return 사용자 ID (존재하지 않으면 Exception 발생)
     */
    Optional<Long> getUserId(String sessionId);

    /**
     * 세션 제거
     * 
     * @param sessionId 웹소켓 세션 ID
     */
    void removeSession(String sessionId);

    /**
     * 특정 사용자의 모든 세션 제거
     * 
     * @param userId 사용자 ID
     */
    void removeUserSessions(Long userId);

    boolean existsSession(String sessionId);
}
