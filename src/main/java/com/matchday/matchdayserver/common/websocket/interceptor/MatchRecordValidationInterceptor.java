package com.matchday.matchdayserver.common.websocket.interceptor;

import com.matchday.matchdayserver.common.Constants;
import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.StatusInterface;
import com.matchday.matchdayserver.common.response.WebSocketStatus;
import com.matchday.matchdayserver.common.websocket.session.WebSocketSessionManager;
import com.matchday.matchdayserver.matchevent.service.MatchEventRecordValidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import com.matchday.matchdayserver.common.response.ApiExceptionResponse;
import com.matchday.matchdayserver.common.response.DefaultStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MatchRecordValidationInterceptor implements ChannelInterceptor {

    @Lazy
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private final WebSocketSessionManager webSocketSessionManager;
    private final MatchEventRecordValidateService matchEventRecordValidateService;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message,
            StompHeaderAccessor.class);

        String sessionId = accessor.getSessionId(); // session ID 없으면 진짜 말도 안되는 상황이라 걍 Exception 던짐

        if (accessor != null) {
            String location = accessor.getDestination();
            if (location != null && location.startsWith("/app/match/")) {
                try {
                    // 여기에 검증 로직을 추가
                    String[] pathParts = location.split("/");
                    if (pathParts.length > 3) {
                        Long matchId = Long.parseLong(pathParts[3]);
                        Long userId = webSocketSessionManager.getUserId(sessionId)
                            .orElseThrow(() -> new ApiException(WebSocketStatus.SESSION_NOT_FOUND));
                        matchEventRecordValidateService.validateEventSavePermission(matchId,
                            userId);
                    }
                } catch (ApiException e) {
                    log.warn("Match validation failed: {}", e.getMessage());
                    ApiExceptionResponse<StatusInterface> errorResponse = ApiExceptionResponse.error(
                        e.getStatus());
                    messagingTemplate.convertAndSendToUser(sessionId,
                        Constants.WEBSOCKET_ERROR_PATH, errorResponse);
                    return null; // 메시지 차단
                } catch (Exception e) {
                    log.error("Error during match validation: ", e);
                    ApiExceptionResponse<StatusInterface> errorResponse = ApiExceptionResponse.error(
                        DefaultStatus.UNKNOWN_ERROR);
                    messagingTemplate.convertAndSendToUser(sessionId,
                        Constants.WEBSOCKET_ERROR_PATH, errorResponse);
                    return null; // 메시지 차단
                }
            }
        }

        return message;
    }
}
