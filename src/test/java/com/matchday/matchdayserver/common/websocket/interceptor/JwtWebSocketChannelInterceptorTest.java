package com.matchday.matchdayserver.common.websocket.interceptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.matchday.matchdayserver.common.exception.ApiException;
import com.matchday.matchdayserver.common.response.AuthStatus;
import com.matchday.matchdayserver.common.websocket.session.WebSocketSessionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class JwtWebSocketChannelInterceptorTest {

  @InjectMocks
  private JwtWebSocketChannelInterceptor interceptor;

  @Mock
  private WebSocketSessionManager sessionManager;

  @Mock
  private MessageChannel channel;

  private Message<?> message;
  private StompHeaderAccessor accessor;

  @BeforeEach
  void setUp() {
    message = MessageBuilder.withPayload("test").build();
    accessor = StompHeaderAccessor.create(StompCommand.CONNECT);
  }

  @Test
  @DisplayName("preSend - CONNECT 명령어 처리 성공")
  void preSend_HandleConnect_Success() {
    // given
    String sessionId = "session123";
    Long userId = 1L;
    Map<String, Object> sessionAttributes = new HashMap<>();
    sessionAttributes.put("userId", userId);

    accessor = StompHeaderAccessor.create(StompCommand.CONNECT);
    accessor.setSessionId(sessionId);
    accessor.setSessionAttributes(sessionAttributes);

    Message<?> messageWithAccessor = MessageBuilder.fromMessage(message)
        .setHeader("stompAccessor", accessor)
        .build();

    // Mock the accessor retrieval
    try (MockedStatic<MessageHeaderAccessor> mockedStatic = mockStatic(MessageHeaderAccessor.class)) {
      mockedStatic.when(() -> MessageHeaderAccessor.getAccessor(messageWithAccessor, StompHeaderAccessor.class))
          .thenReturn(accessor);

      // when
      Message<?> result = interceptor.preSend(messageWithAccessor, channel);

      // then
      assertNotNull(result);
      assertEquals(messageWithAccessor, result);
      verify(sessionManager).putSession(sessionId, userId);
    }
  }

  @Test
  @DisplayName("preSend - CONNECT 명령어 처리 실패: 세션 속성이 null")
  void preSend_HandleConnect_FailSessionAttributesNull() {
    // given
    String sessionId = "session123";

    accessor = StompHeaderAccessor.create(StompCommand.CONNECT);
    accessor.setSessionId(sessionId);
    accessor.setSessionAttributes(null);

    Message<?> messageWithAccessor = MessageBuilder.fromMessage(message)
        .setHeader("stompAccessor", accessor)
        .build();

    try (MockedStatic<MessageHeaderAccessor> mockedStatic = mockStatic(MessageHeaderAccessor.class)) {
      mockedStatic.when(() -> MessageHeaderAccessor.getAccessor(messageWithAccessor, StompHeaderAccessor.class))
          .thenReturn(accessor);

      // when & then
      ApiException exception = assertThrows(ApiException.class,
          () -> interceptor.preSend(messageWithAccessor, channel));

      assertEquals(AuthStatus.UNAUTHORIZED, exception.getStatus());
      verify(sessionManager, never()).putSession(anyString(), anyLong());
    }
  }

  @Test
  @DisplayName("preSend - CONNECT 명령어 처리 실패: userId가 null")
  void preSend_HandleConnect_FailUserIdNull() {
    // given
    String sessionId = "session123";
    Map<String, Object> sessionAttributes = new HashMap<>();
    // userId를 추가하지 않음

    accessor = StompHeaderAccessor.create(StompCommand.CONNECT);
    accessor.setSessionId(sessionId);
    accessor.setSessionAttributes(sessionAttributes);

    Message<?> messageWithAccessor = MessageBuilder.fromMessage(message)
        .setHeader("stompAccessor", accessor)
        .build();

    try (MockedStatic<MessageHeaderAccessor> mockedStatic = mockStatic(MessageHeaderAccessor.class)) {
      mockedStatic.when(() -> MessageHeaderAccessor.getAccessor(messageWithAccessor, StompHeaderAccessor.class))
          .thenReturn(accessor);

      // when & then
      ApiException exception = assertThrows(ApiException.class,
          () -> interceptor.preSend(messageWithAccessor, channel));

      assertEquals(AuthStatus.UNAUTHORIZED, exception.getStatus());
      verify(sessionManager, never()).putSession(anyString(), anyLong());
    }
  }

  @Test
  @DisplayName("preSend - CONNECT 명령어 처리 실패: Exception 발생")
  void preSend_HandleConnect_FailException() {
    // given
    String sessionId = "session123";
    Long userId = 1L;
    Map<String, Object> sessionAttributes = new HashMap<>();
    sessionAttributes.put("userId", userId);

    accessor = StompHeaderAccessor.create(StompCommand.CONNECT);
    accessor.setSessionId(sessionId);
    accessor.setSessionAttributes(sessionAttributes);

    Message<?> messageWithAccessor = MessageBuilder.fromMessage(message)
        .setHeader("stompAccessor", accessor)
        .build();

    // sessionManager에서 예외 발생하도록 설정
    doThrow(new RuntimeException("Database error")).when(sessionManager).putSession(sessionId, userId);

    try (MockedStatic<MessageHeaderAccessor> mockedStatic = mockStatic(MessageHeaderAccessor.class)) {
      mockedStatic.when(() -> MessageHeaderAccessor.getAccessor(messageWithAccessor, StompHeaderAccessor.class))
          .thenReturn(accessor);

      // when & then
      ApiException exception = assertThrows(ApiException.class,
          () -> interceptor.preSend(messageWithAccessor, channel));

      assertEquals(AuthStatus.UNAUTHORIZED, exception.getStatus());
      verify(sessionManager).putSession(sessionId, userId);
    }
  }

  @Test
  @DisplayName("preSend - CONNECT 명령어 처리 실패: userId 타입 캐스팅 실패")
  void preSend_HandleConnect_FailUserIdCasting() {
    // given
    String sessionId = "session123";
    Map<String, Object> sessionAttributes = new HashMap<>();
    sessionAttributes.put("userId", "invalidUserId"); // String으로 설정하여 Long 캐스팅 실패 유발

    accessor = StompHeaderAccessor.create(StompCommand.CONNECT);
    accessor.setSessionId(sessionId);
    accessor.setSessionAttributes(sessionAttributes);

    Message<?> messageWithAccessor = MessageBuilder.fromMessage(message)
        .setHeader("stompAccessor", accessor)
        .build();

    try (MockedStatic<MessageHeaderAccessor> mockedStatic = mockStatic(MessageHeaderAccessor.class)) {
      mockedStatic.when(() -> MessageHeaderAccessor.getAccessor(messageWithAccessor, StompHeaderAccessor.class))
          .thenReturn(accessor);

      // when & then
      ApiException exception = assertThrows(ApiException.class,
          () -> interceptor.preSend(messageWithAccessor, channel));

      assertEquals(AuthStatus.UNAUTHORIZED, exception.getStatus());
      verify(sessionManager, never()).putSession(anyString(), anyLong());
    }
  }

  @Test
  @DisplayName("preSend - SEND 명령어 처리 성공")
  void preSend_HandleSend_Success() {
    // given
    String sessionId = "session123";
    Long userId = 1L;

    accessor = StompHeaderAccessor.create(StompCommand.SEND);
    accessor.setSessionId(sessionId);

    Message<?> messageWithAccessor = MessageBuilder.fromMessage(message)
        .setHeader("stompAccessor", accessor)
        .build();

    given(sessionManager.existsSession(sessionId)).willReturn(true);
    given(sessionManager.getUserId(sessionId)).willReturn(Optional.of(userId));

    try (MockedStatic<MessageHeaderAccessor> mockedStatic = mockStatic(MessageHeaderAccessor.class)) {
      mockedStatic.when(() -> MessageHeaderAccessor.getAccessor(messageWithAccessor, StompHeaderAccessor.class))
          .thenReturn(accessor);

      // when
      Message<?> result = interceptor.preSend(messageWithAccessor, channel);

      // then
      assertNotNull(result);
      assertEquals(messageWithAccessor, result);
      verify(sessionManager).existsSession(sessionId);
      verify(sessionManager).getUserId(sessionId);
    }
  }

  @Test
  @DisplayName("preSend - SEND 명령어 처리 실패: 세션 ID가 null")
  void preSend_HandleSend_FailSessionIdNull() {
    // given
    accessor = StompHeaderAccessor.create(StompCommand.SEND);
    accessor.setSessionId(null);

    Message<?> messageWithAccessor = MessageBuilder.fromMessage(message)
        .setHeader("stompAccessor", accessor)
        .build();

    try (MockedStatic<MessageHeaderAccessor> mockedStatic = mockStatic(MessageHeaderAccessor.class)) {
      mockedStatic.when(() -> MessageHeaderAccessor.getAccessor(messageWithAccessor, StompHeaderAccessor.class))
          .thenReturn(accessor);

      // when & then
      ApiException exception = assertThrows(ApiException.class,
          () -> interceptor.preSend(messageWithAccessor, channel));

      assertEquals(AuthStatus.UNAUTHORIZED, exception.getStatus());
      verify(sessionManager, never()).existsSession(anyString());
    }
  }

  @Test
  @DisplayName("preSend - SEND 명령어 처리 실패: 유효하지 않은 세션")
  void preSend_HandleSend_FailInvalidSession() {
    // given
    String sessionId = "session123";

    accessor = StompHeaderAccessor.create(StompCommand.SEND);
    accessor.setSessionId(sessionId);

    Message<?> messageWithAccessor = MessageBuilder.fromMessage(message)
        .setHeader("stompAccessor", accessor)
        .build();

    given(sessionManager.existsSession(sessionId)).willReturn(false);

    try (MockedStatic<MessageHeaderAccessor> mockedStatic = mockStatic(MessageHeaderAccessor.class)) {
      mockedStatic.when(() -> MessageHeaderAccessor.getAccessor(messageWithAccessor, StompHeaderAccessor.class))
          .thenReturn(accessor);

      // when & then
      ApiException exception = assertThrows(ApiException.class,
          () -> interceptor.preSend(messageWithAccessor, channel));

      assertEquals(AuthStatus.UNAUTHORIZED, exception.getStatus());
      verify(sessionManager).existsSession(sessionId);
      verify(sessionManager, never()).getUserId(anyString());
    }
  }

  @Test
  @DisplayName("preSend - SEND 명령어 처리: getUserId가 빈 Optional 반환")
  void preSend_HandleSend_GetUserIdEmpty() {
    // given
    String sessionId = "session123";

    accessor = StompHeaderAccessor.create(StompCommand.SEND);
    accessor.setSessionId(sessionId);

    Message<?> messageWithAccessor = MessageBuilder.fromMessage(message)
        .setHeader("stompAccessor", accessor)
        .build();

    given(sessionManager.existsSession(sessionId)).willReturn(true);
    given(sessionManager.getUserId(sessionId)).willReturn(Optional.empty());

    try (MockedStatic<MessageHeaderAccessor> mockedStatic = mockStatic(MessageHeaderAccessor.class)) {
      mockedStatic.when(() -> MessageHeaderAccessor.getAccessor(messageWithAccessor, StompHeaderAccessor.class))
          .thenReturn(accessor);

      // when
      Message<?> result = interceptor.preSend(messageWithAccessor, channel);

      // then
      assertNotNull(result);
      assertEquals(messageWithAccessor, result);
      verify(sessionManager).existsSession(sessionId);
      verify(sessionManager).getUserId(sessionId);
    }
  }

  @Test
  @DisplayName("preSend - DISCONNECT 명령어 처리 성공")
  void preSend_HandleDisconnect_Success() {
    // given
    String sessionId = "session123";
    Long userId = 1L;

    accessor = StompHeaderAccessor.create(StompCommand.DISCONNECT);
    accessor.setSessionId(sessionId);

    Message<?> messageWithAccessor = MessageBuilder.fromMessage(message)
        .setHeader("stompAccessor", accessor)
        .build();

    given(sessionManager.getUserId(sessionId)).willReturn(Optional.of(userId));

    try (MockedStatic<MessageHeaderAccessor> mockedStatic = mockStatic(MessageHeaderAccessor.class)) {
      mockedStatic.when(() -> MessageHeaderAccessor.getAccessor(messageWithAccessor, StompHeaderAccessor.class))
          .thenReturn(accessor);

      // when
      Message<?> result = interceptor.preSend(messageWithAccessor, channel);

      // then
      assertNotNull(result);
      assertEquals(messageWithAccessor, result);
      verify(sessionManager).getUserId(sessionId);
      verify(sessionManager).removeSession(sessionId);
    }
  }

  @Test
  @DisplayName("preSend - DISCONNECT 명령어 처리: 세션 ID가 null")
  void preSend_HandleDisconnect_SessionIdNull() {
    // given
    accessor = StompHeaderAccessor.create(StompCommand.DISCONNECT);
    accessor.setSessionId(null);

    Message<?> messageWithAccessor = MessageBuilder.fromMessage(message)
        .setHeader("stompAccessor", accessor)
        .build();

    try (MockedStatic<MessageHeaderAccessor> mockedStatic = mockStatic(MessageHeaderAccessor.class)) {
      mockedStatic.when(() -> MessageHeaderAccessor.getAccessor(messageWithAccessor, StompHeaderAccessor.class))
          .thenReturn(accessor);

      // when
      Message<?> result = interceptor.preSend(messageWithAccessor, channel);

      // then
      assertNotNull(result);
      assertEquals(messageWithAccessor, result);
      verify(sessionManager, never()).getUserId(anyString());
      verify(sessionManager, never()).removeSession(anyString());
    }
  }

  @Test
  @DisplayName("preSend - DISCONNECT 명령어 처리: getUserId가 빈 Optional 반환")
  void preSend_HandleDisconnect_GetUserIdEmpty() {
    // given
    String sessionId = "session123";

    accessor = StompHeaderAccessor.create(StompCommand.DISCONNECT);
    accessor.setSessionId(sessionId);

    Message<?> messageWithAccessor = MessageBuilder.fromMessage(message)
        .setHeader("stompAccessor", accessor)
        .build();

    given(sessionManager.getUserId(sessionId)).willReturn(Optional.empty());

    try (MockedStatic<MessageHeaderAccessor> mockedStatic = mockStatic(MessageHeaderAccessor.class)) {
      mockedStatic.when(() -> MessageHeaderAccessor.getAccessor(messageWithAccessor, StompHeaderAccessor.class))
          .thenReturn(accessor);

      // when
      Message<?> result = interceptor.preSend(messageWithAccessor, channel);

      // then
      assertNotNull(result);
      assertEquals(messageWithAccessor, result);
      verify(sessionManager).getUserId(sessionId);
      verify(sessionManager).removeSession(sessionId);
    }
  }

  @Test
  @DisplayName("preSend - 기타 명령어 처리 (무시)")
  void preSend_HandleOtherCommand_Ignore() {
    // given
    accessor = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
    accessor.setSessionId("session123");

    Message<?> messageWithAccessor = MessageBuilder.fromMessage(message)
        .setHeader("stompAccessor", accessor)
        .build();

    try (MockedStatic<MessageHeaderAccessor> mockedStatic = mockStatic(MessageHeaderAccessor.class)) {
      mockedStatic.when(() -> MessageHeaderAccessor.getAccessor(messageWithAccessor, StompHeaderAccessor.class))
          .thenReturn(accessor);

      // when
      Message<?> result = interceptor.preSend(messageWithAccessor, channel);

      // then
      assertNotNull(result);
      assertEquals(messageWithAccessor, result);
      verifyNoInteractions(sessionManager);
    }
  }

  @Test
  @DisplayName("preSend - accessor가 null인 경우")
  void preSend_AccessorNull() {
    // given
    Message<?> messageWithAccessor = MessageBuilder.fromMessage(message).build();

    try (MockedStatic<MessageHeaderAccessor> mockedStatic = mockStatic(MessageHeaderAccessor.class)) {
      mockedStatic.when(() -> MessageHeaderAccessor.getAccessor(messageWithAccessor, StompHeaderAccessor.class))
          .thenReturn(null);

      // when
      Message<?> result = interceptor.preSend(messageWithAccessor, channel);

      // then
      assertNotNull(result);
      assertEquals(messageWithAccessor, result);
      verifyNoInteractions(sessionManager);
    }
  }

  @Test
  @DisplayName("preSend - command가 null인 경우")
  void preSend_CommandNull() {
    // given
    accessor = StompHeaderAccessor.create(StompCommand.CONNECT); // 일단 생성하고
    accessor.setSessionId("session123");

    Message<?> messageWithAccessor = MessageBuilder.fromMessage(message)
        .setHeader("stompAccessor", accessor)
        .build();

    // Mock으로 command가 null인 accessor를 반환하도록 설정
    StompHeaderAccessor nullCommandAccessor = mock(StompHeaderAccessor.class);
    given(nullCommandAccessor.getCommand()).willReturn(null);
    given(nullCommandAccessor.getSessionId()).willReturn("session123");

    try (MockedStatic<MessageHeaderAccessor> mockedStatic = mockStatic(MessageHeaderAccessor.class)) {
      mockedStatic.when(() -> MessageHeaderAccessor.getAccessor(messageWithAccessor, StompHeaderAccessor.class))
          .thenReturn(nullCommandAccessor);

      // when
      Message<?> result = interceptor.preSend(messageWithAccessor, channel);

      // then
      assertNotNull(result);
      assertEquals(messageWithAccessor, result);
      verifyNoInteractions(sessionManager);
    }
  }
}
