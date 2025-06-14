package com.matchday.matchdayserver.common.websocket.interceptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import com.matchday.matchdayserver.auth.model.dto.enums.JwtTokenType;
import com.matchday.matchdayserver.common.auth.TokenHelper;
import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.JwtStatus;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
class JwtWebSocketHandshakeInterceptorTest {

    @Mock
    private TokenHelper tokenHelper;

    @Mock
    private ServerHttpRequest request;

    @Mock
    private ServerHttpResponse response;

    @Mock
    private WebSocketHandler wsHandler;

    @Mock
    private HttpHeaders httpHeaders;

    @InjectMocks
    private JwtWebSocketHandshakeInterceptor interceptor;

    private Map<String, Object> attributes;
    private String validToken;
    private Long userId;

    @BeforeEach
    void setUp() {
        attributes = new HashMap<>();
        validToken = "valid.jwt.token";
        userId = 1L;
    }

    @Test
    @DisplayName("beforeHandshake - 성공 케이스")
    void beforeHandshake_Success() {
        // given
        String authHeader = "Bearer " + validToken;
        Claims claims = mock(Claims.class);
        given(claims.get("userId")).willReturn(userId);

        given(request.getURI()).willReturn(URI.create("/ws/test"));
        given(request.getHeaders()).willReturn(httpHeaders);
        given(httpHeaders.getFirst("Authorization")).willReturn(authHeader);
        given(tokenHelper.validateToken(validToken, JwtTokenType.ACCESS)).willReturn(true);
        given(tokenHelper.getClaims(validToken)).willReturn(claims);

        // when
        boolean result = interceptor.beforeHandshake(request, response, wsHandler, attributes);

        // then
        assertTrue(result);
        assertEquals(userId, attributes.get("userId"));
        assertEquals(validToken, attributes.get("token"));

        verify(tokenHelper).validateToken(validToken, JwtTokenType.ACCESS);
        verify(tokenHelper).getClaims(validToken);
        verifyNoInteractions(response);
    }

    @Test
    @DisplayName("beforeHandshake - Authorization 헤더가 없는 경우")
    void beforeHandshake_NoAuthorizationHeader() {
        // given
        given(request.getURI()).willReturn(URI.create("/ws/test"));
        given(request.getHeaders()).willReturn(httpHeaders);
        given(httpHeaders.getFirst("Authorization")).willReturn(null);

        // when
        boolean result = interceptor.beforeHandshake(request, response, wsHandler, attributes);

        // then
        assertFalse(result);
        assertTrue(attributes.isEmpty());

        verify(response).setStatusCode(HttpStatus.UNAUTHORIZED);
        verifyNoInteractions(tokenHelper);
    }

    @Test
    @DisplayName("beforeHandshake - Bearer 형식이 아닌 경우")
    void beforeHandshake_InvalidBearerFormat() {
        // given
        String invalidAuthHeader = "Basic " + validToken;

        given(request.getURI()).willReturn(URI.create("/ws/test"));
        given(request.getHeaders()).willReturn(httpHeaders);
        given(httpHeaders.getFirst("Authorization")).willReturn(invalidAuthHeader);

        // when
        boolean result = interceptor.beforeHandshake(request, response, wsHandler, attributes);

        // then
        assertFalse(result);
        assertTrue(attributes.isEmpty());

        verify(response).setStatusCode(HttpStatus.UNAUTHORIZED);
        verifyNoInteractions(tokenHelper);
    }

    @Test
    @DisplayName("beforeHandshake - Bearer 토큰이 너무 짧은 경우")
    void beforeHandshake_TooShortBearerToken() {
        // given
        String shortAuthHeader = "Bearer";

        given(request.getURI()).willReturn(URI.create("/ws/test"));
        given(request.getHeaders()).willReturn(httpHeaders);
        given(httpHeaders.getFirst("Authorization")).willReturn(shortAuthHeader);

        // when
        boolean result = interceptor.beforeHandshake(request, response, wsHandler, attributes);

        // then
        assertFalse(result);
        assertTrue(attributes.isEmpty());

        verify(response).setStatusCode(HttpStatus.UNAUTHORIZED);
        verifyNoInteractions(tokenHelper);
    }

    @Test
    @DisplayName("beforeHandshake - 토큰 검증 실패 (validateToken이 false 반환)")
    void beforeHandshake_TokenValidationFailed() {
        // given
        String authHeader = "Bearer " + validToken;

        given(request.getURI()).willReturn(URI.create("/ws/test"));
        given(request.getHeaders()).willReturn(httpHeaders);
        given(httpHeaders.getFirst("Authorization")).willReturn(authHeader);
        given(tokenHelper.validateToken(validToken, JwtTokenType.ACCESS)).willReturn(false);

        // when
        boolean result = interceptor.beforeHandshake(request, response, wsHandler, attributes);

        // then
        assertFalse(result);
        assertTrue(attributes.isEmpty());

        verify(tokenHelper).validateToken(validToken, JwtTokenType.ACCESS);
        verify(response).setStatusCode(HttpStatus.UNAUTHORIZED);
        verify(tokenHelper, never()).getClaims(anyString());
    }

    @Test
    @DisplayName("beforeHandshake - 토큰 검증 시 ApiException 발생")
    void beforeHandshake_TokenValidationApiException() {
        // given
        String authHeader = "Bearer " + validToken;
        ApiException apiException = new ApiException(JwtStatus.EXPIRED_ACCESS_TOKEN);

        given(request.getURI()).willReturn(URI.create("/ws/test"));
        given(request.getHeaders()).willReturn(httpHeaders);
        given(httpHeaders.getFirst("Authorization")).willReturn(authHeader);
        given(tokenHelper.validateToken(validToken, JwtTokenType.ACCESS)).willThrow(apiException);

        // when
        boolean result = interceptor.beforeHandshake(request, response, wsHandler, attributes);

        // then
        assertFalse(result);
        assertTrue(attributes.isEmpty());

        verify(tokenHelper).validateToken(validToken, JwtTokenType.ACCESS);
        verify(response).setStatusCode(HttpStatus.UNAUTHORIZED);
        verify(tokenHelper, never()).getClaims(anyString());
    }

    @Test
    @DisplayName("beforeHandshake - getClaims에서 예외 발생")
    void beforeHandshake_GetClaimsException() {
        // given
        String authHeader = "Bearer " + validToken;
        RuntimeException exception = new RuntimeException("Claims parsing error");

        given(request.getURI()).willReturn(URI.create("/ws/test"));
        given(request.getHeaders()).willReturn(httpHeaders);
        given(httpHeaders.getFirst("Authorization")).willReturn(authHeader);
        given(tokenHelper.validateToken(validToken, JwtTokenType.ACCESS)).willReturn(true);
        given(tokenHelper.getClaims(validToken)).willThrow(exception);

        // when
        boolean result = interceptor.beforeHandshake(request, response, wsHandler, attributes);

        // then
        assertFalse(result);
        assertTrue(attributes.isEmpty());

        verify(tokenHelper).validateToken(validToken, JwtTokenType.ACCESS);
        verify(tokenHelper).getClaims(validToken);
        verify(response).setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("beforeHandshake - userId 추출 실패 (NumberFormatException)")
    void beforeHandshake_UserIdExtractionFailed() {
        // given
        String authHeader = "Bearer " + validToken;
        Claims claims = mock(Claims.class);
        given(claims.get("userId")).willReturn("invalid_number");

        given(request.getURI()).willReturn(URI.create("/ws/test"));
        given(request.getHeaders()).willReturn(httpHeaders);
        given(httpHeaders.getFirst("Authorization")).willReturn(authHeader);
        given(tokenHelper.validateToken(validToken, JwtTokenType.ACCESS)).willReturn(true);
        given(tokenHelper.getClaims(validToken)).willReturn(claims);

        // when
        boolean result = interceptor.beforeHandshake(request, response, wsHandler, attributes);

        // then
        assertFalse(result);
        assertTrue(attributes.isEmpty());

        verify(tokenHelper).validateToken(validToken, JwtTokenType.ACCESS);
        verify(tokenHelper).getClaims(validToken);
        verify(response).setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("beforeHandshake - userId가 null인 경우")
    void beforeHandshake_UserIdNull() {
        // given
        String authHeader = "Bearer " + validToken;
        Claims claims = mock(Claims.class);
        given(claims.get("userId")).willReturn(null);

        given(request.getURI()).willReturn(URI.create("/ws/test"));
        given(request.getHeaders()).willReturn(httpHeaders);
        given(httpHeaders.getFirst("Authorization")).willReturn(authHeader);
        given(tokenHelper.validateToken(validToken, JwtTokenType.ACCESS)).willReturn(true);
        given(tokenHelper.getClaims(validToken)).willReturn(claims);

        // when
        boolean result = interceptor.beforeHandshake(request, response, wsHandler, attributes);

        // then
        assertFalse(result);
        assertTrue(attributes.isEmpty());

        verify(tokenHelper).validateToken(validToken, JwtTokenType.ACCESS);
        verify(tokenHelper).getClaims(validToken);
        verify(response).setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("beforeHandshake - validateToken에서 일반 Exception 발생")
    void beforeHandshake_ValidateTokenGeneralException() {
        // given
        String authHeader = "Bearer " + validToken;
        RuntimeException exception = new RuntimeException("Unexpected error");

        given(request.getURI()).willReturn(URI.create("/ws/test"));
        given(request.getHeaders()).willReturn(httpHeaders);
        given(httpHeaders.getFirst("Authorization")).willReturn(authHeader);
        given(tokenHelper.validateToken(validToken, JwtTokenType.ACCESS)).willThrow(exception);

        // when
        boolean result = interceptor.beforeHandshake(request, response, wsHandler, attributes);

        // then
        assertFalse(result);
        assertTrue(attributes.isEmpty());

        verify(tokenHelper).validateToken(validToken, JwtTokenType.ACCESS);
        verify(response).setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        verify(tokenHelper, never()).getClaims(anyString());
    }

    @Test
    @DisplayName("afterHandshake - 정상 완료 (exception이 null)")
    void afterHandshake_Success() {
        // given
        // afterHandshake 메서드에서는 mock 객체의 어떤 메서드도 호출하지 않음

        // when & then
        assertDoesNotThrow(() -> 
            interceptor.afterHandshake(request, response, wsHandler, null)
        );
    }

    @Test
    @DisplayName("afterHandshake - 예외가 있는 경우")
    void afterHandshake_WithException() {
        // given
        Exception exception = new RuntimeException("Handshake failed");
        // afterHandshake 메서드에서는 mock 객체의 어떤 메서드도 호출하지 않음

        // when & then
        assertDoesNotThrow(() -> 
            interceptor.afterHandshake(request, response, wsHandler, exception)
        );
    }

    @Test
    @DisplayName("beforeHandshake - 빈 Authorization 헤더")
    void beforeHandshake_EmptyAuthorizationHeader() {
        // given
        given(request.getURI()).willReturn(URI.create("/ws/test"));
        given(request.getHeaders()).willReturn(httpHeaders);
        given(httpHeaders.getFirst("Authorization")).willReturn("");

        // when
        boolean result = interceptor.beforeHandshake(request, response, wsHandler, attributes);

        // then
        assertFalse(result);
        assertTrue(attributes.isEmpty());

        verify(response).setStatusCode(HttpStatus.UNAUTHORIZED);
        verifyNoInteractions(tokenHelper);
    }

    @Test
    @DisplayName("beforeHandshake - 공백만 있는 Authorization 헤더")
    void beforeHandshake_WhitespaceOnlyAuthorizationHeader() {
        // given
        given(request.getURI()).willReturn(URI.create("/ws/test"));
        given(request.getHeaders()).willReturn(httpHeaders);
        given(httpHeaders.getFirst("Authorization")).willReturn("   ");

        // when
        boolean result = interceptor.beforeHandshake(request, response, wsHandler, attributes);

        // then
        assertFalse(result);
        assertTrue(attributes.isEmpty());

        verify(response).setStatusCode(HttpStatus.UNAUTHORIZED);
        verifyNoInteractions(tokenHelper);
    }
}
