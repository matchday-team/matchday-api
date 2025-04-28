package com.matchday.matchdayserver.common.exception;

import java.io.IOException;
import java.net.SocketException;
import java.util.List;

import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.ControllerAdvice;

import com.matchday.matchdayserver.common.response.ApiResponse;
import com.matchday.matchdayserver.common.response.DefaultStatus;
import com.matchday.matchdayserver.common.response.MatchStatus;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class WebSocketExceptionHandler {

  private final CustomWebSocketHandlerDecorator decorator;

  /**
   * Socket Exception은 이미 닫힌 세션에서 발생하기 때문에
   * 리소스 절약을 위해서 세션을 수동으로 닫아주는게 중요함
   * Ref: https://recordsoflife.tistory.com/497
   * Ref:
   * https://github.com/Attica-org/athens-backend/blob/a795cdbf1d8edb85b78f5b42c21b892afeb3971c/src/main/java/com/attica/athens/global/handler/WebSocketExceptionHandler.java#L27
   * 
   * @param message
   * @throws IOException
   */
  @MessageExceptionHandler(SocketException.class)
  // user prefix는 자동으로 붙음, 전체 유저에게 전송하고 싶으면 SendTo()를 써야함
  @SendToUser("/queue/errors")
  public ApiResponse<ApiExceptionInterface> handleSocketException(Message<?> message) throws IOException {
    removeSession(message);
    return ApiResponse.error(MatchStatus.SOCKET_ERROR);
  }

  @MessageExceptionHandler(ApiException.class)
  @SendToUser("/queue/errors")
  public ApiResponse<ApiExceptionInterface> handleApiException(ApiException exception) {
    log.error("WebSocket API Exception: {}", exception.getMessage());
    return ApiResponse.error(exception.getStatus());
  }

  @MessageExceptionHandler(Exception.class)
  @SendToUser("/queue/errors")
  public ApiResponse<ApiExceptionInterface> handleGeneralException(Exception exception) {
    log.error("WebSocket General Exception: {}", exception.getMessage());
    return ApiResponse.error(DefaultStatus.UNKNOWN_ERROR);
  }

  private void removeSession(Message<?> message) throws IOException {
    SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(message);
    String sessionId = accessor.getSessionId();
    log.info("session = {}, connection remove", sessionId);
    decorator.closeSession(sessionId);
  }
}
