package com.matchday.matchdayserver.common.exception;

import java.util.concurrent.ConcurrentHashMap;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;

import lombok.extern.slf4j.Slf4j;

/**
 * 웹소켓 세션이 ws로 업그레이드 되기 전 HTTP 세션 ID를 저장하고 세션을 닫을 때 사용하기 위한 데코레이터
 */
@Slf4j
@Component
public class CustomWebSocketHandlerDecorator extends WebSocketHandlerDecorator {

  private final ConcurrentHashMap<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

  public CustomWebSocketHandlerDecorator(
      // STOMP와 같은 서브 프로토콜을 지원하는 WebSocket 통신을 처리하기 위한 핸들러
      @Qualifier("subProtocolWebSocketHandler") WebSocketHandler delegate) {
    super(delegate);
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    sessionMap.put(session.getId(), session);
    super.afterConnectionEstablished(session);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus)
      throws Exception {
    sessionMap.remove(session.getId());
    super.afterConnectionClosed(session, closeStatus);
  }

  public void closeSession(String sessionId) throws IOException {
    WebSocketSession session = sessionMap.get(sessionId);
    if (session != null && session.isOpen()) {
      log.error("session = {}, connection close", sessionId);
      session.close();
    }
  }
}