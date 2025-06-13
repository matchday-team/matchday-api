package com.matchday.matchdayserver.common.websocket.interceptor;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.AuthStatus;
import com.matchday.matchdayserver.common.websocket.session.WebSocketSessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 웹소켓 메시지 채널 인터셉터 STOMP 메시지 전송 시 인증 확인 및 세션 관리
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtWebSocketChannelInterceptor implements ChannelInterceptor {

    private final WebSocketSessionManager sessionManager;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message,
            StompHeaderAccessor.class);

        if (accessor != null) {
            StompCommand command = accessor.getCommand();
            String sessionId = accessor.getSessionId();

            log.debug("Processing STOMP command: {}, sessionId: {}", command, sessionId);

            if (command != null) {
                switch (command) {
                    case CONNECT:
                        handleConnect(accessor);
                        break;
                    case SEND:
                        handleSend(accessor, sessionId);
                        break;
                    case DISCONNECT:
                        handleDisconnect(sessionId);
                        break;
                    default:
                        break;
                }
            }
        }

        return message;
    }

    /**
     * CONNECT 명령 처리 핸드셰이크에서 검증된 사용자 정보를 세션에 저장
     */
    private void handleConnect(StompHeaderAccessor accessor) {
        try {
            String sessionId = accessor.getSessionId();

            // 세션 attributes에서 사용자 정보 가져오기 (핸드셰이크에서 저장됨)
            Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
            if (sessionAttributes == null) {
                log.error("Session attributes not found for sessionId: {}", sessionId);
                throw new ApiException(AuthStatus.UNAUTHORIZED);
            }

            Object userIdObj = sessionAttributes.get("userId");

            if (userIdObj == null) {
                log.error("UserId not found in session attributes for sessionId: {}", sessionId);
                throw new ApiException(AuthStatus.UNAUTHORIZED);
            }

            Long userId = (Long) userIdObj;

            // 세션 매핑 저장
            sessionManager.putSession(sessionId, userId);

            log.info("User connected to WebSocket: userId={}, sessionId={}", userId, sessionId);

        } catch (Exception e) {
            log.error("Error handling CONNECT command: {}", e.getMessage(), e);
            throw new ApiException(AuthStatus.UNAUTHORIZED);
        }
    }

    /**
     * SEND 명령 처리 메시지 전송 시 세션 유효성 확인
     */
    private void handleSend(StompHeaderAccessor accessor, String sessionId) {
        if (sessionId == null) {
            log.warn("Session ID is null for SEND command");
            throw new ApiException(AuthStatus.UNAUTHORIZED);
        }

        // 세션이 유효한지 확인
        if (!sessionManager.existsSession(sessionId)) {
            log.warn("Invalid session for SEND command: sessionId={}", sessionId);
            throw new ApiException(AuthStatus.UNAUTHORIZED);
        }

        // 사용자 정보를 헤더에 추가 (메시지 핸들러에서 사용할 수 있도록)
        sessionManager.getUserId(sessionId)
            .ifPresent(userId -> accessor.setUser(() -> userId.toString()));
    }

    /**
     * DISCONNECT 명령 처리 세션 정리
     */
    private void handleDisconnect(String sessionId) {
        if (sessionId != null) {
            sessionManager.getUserId(sessionId)
                .ifPresent(userId ->
                    log.info("User disconnected from WebSocket: userId={}, sessionId={}", userId,
                        sessionId));

            sessionManager.removeSession(sessionId);
        }
    }

    @Override
    public void postSend(@NonNull Message<?> message, @NonNull MessageChannel channel,
        boolean sent) {
        return;
    }

    @Override
    public boolean preReceive(@NonNull MessageChannel channel) {
        return true;
    }

    @Override
    public Message<?> postReceive(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        return message;
    }

    @Override
    public void afterSendCompletion(@NonNull Message<?> message, @NonNull MessageChannel channel,
        boolean sent, @Nullable Exception ex) {
        if (ex != null) {
            log.error("Message send failed: {}", ex.getMessage(), ex);
        }
    }
}
