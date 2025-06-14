package com.matchday.matchdayserver.common.websocket.interceptor;

import com.matchday.matchdayserver.auth.model.dto.enums.JwtTokenType;
import com.matchday.matchdayserver.common.auth.TokenHelper;
import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.JwtStatus;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * 웹소켓 핸드셰이크 시 JWT 토큰 검증 인터셉터
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtWebSocketHandshakeInterceptor implements HandshakeInterceptor {

    private final TokenHelper tokenHelper;

    @Override
    public boolean beforeHandshake(@NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler, @NonNull Map<String, Object> attributes) {

        log.info("WebSocket handshake started for URI: {}", request.getURI());

        try {
            String token = extractTokenFromRequest(request);
            if (token == null) {
                log.warn("JWT token not found in WebSocket handshake request");
                throw new ApiException(JwtStatus.UNAUTHORIZED);
            }

            // JWT 토큰 검증
            if (!tokenHelper.validateToken(token, JwtTokenType.ACCESS)) {
                log.warn("Invalid JWT token in WebSocket handshake");
                throw new ApiException(JwtStatus.INVALID_TOKEN);
            }

            // 사용자 정보 추출
            Claims claims = tokenHelper.getClaims(token);
            Long userId = Long.valueOf(claims.get("userId").toString());

            // 세션 attributes에 사용자 정보 저장
            attributes.put("userId", userId);
            attributes.put("token", token);

            log.info("WebSocket handshake successful for userId: {}", userId);
            return true;

        } catch (ApiException e) {
            log.error("JWT validation failed during WebSocket handshake: {}", e.getMessage());
            response.setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            return false;
        } catch (Exception e) {
            log.error("Unexpected error during WebSocket handshake: {}", e.getMessage(), e);
            response.setStatusCode(org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
            return false;
        }
    }

    @Override
    public void afterHandshake(@NonNull ServerHttpRequest request,
            @NonNull ServerHttpResponse response,
            @NonNull WebSocketHandler wsHandler, @Nullable Exception exception) {
        if (exception != null) {
            log.error("WebSocket handshake failed: {}", exception.getMessage(), exception);
        } else {
            log.info("WebSocket handshake completed successfully");
        }
    }

    /**
     * 요청에서 JWT 토큰 추출
     */
    private String extractTokenFromRequest(ServerHttpRequest request) {
        String authHeader = request.getHeaders().getFirst("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }
}
