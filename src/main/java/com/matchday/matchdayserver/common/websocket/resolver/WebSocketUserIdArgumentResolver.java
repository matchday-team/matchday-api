package com.matchday.matchdayserver.common.websocket.resolver;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.WebSocketStatus;
import com.matchday.matchdayserver.common.websocket.session.WebSocketSessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;

/**
 * 웹소켓에서 사용자 ID를 추출하는 Argument Resolver
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketUserIdArgumentResolver implements HandlerMethodArgumentResolver {

    private final WebSocketSessionManager webSocketSessionManager;

    @Override
    public boolean supportsParameter(@NonNull MethodParameter parameter) {
        return parameter.hasParameterAnnotation(WebSocketUserId.class)
                && parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter, @NonNull Message<?> message) throws Exception {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(message);
        String sessionId = headerAccessor.getSessionId();

        try {
            return webSocketSessionManager.getUserId(sessionId);
        } catch (Exception e) {
            log.error("Unexpected error resolving user ID: {}", e.getMessage());
            throw new ApiException(WebSocketStatus.SESSION_NOT_FOUND);
        }
    }
}
