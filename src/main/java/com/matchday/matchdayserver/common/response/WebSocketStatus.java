package com.matchday.matchdayserver.common.response;

public enum WebSocketStatus implements StatusInterface {
  SESSION_NOT_FOUND(404, 12001, "WebSocket 세션을 찾을 수 없습니다"),
  ;

  private final int httpStatusCode;
  private final int customStatusCode;
  private final String description;

  WebSocketStatus(int httpStatusCode, int customStatusCode, String description) {
    this.httpStatusCode = httpStatusCode;
    this.customStatusCode = customStatusCode;
    this.description = description;
  }

  @Override
  public int getHttpStatusCode() {
    return this.httpStatusCode;
  }

  @Override
  public int getCustomStatusCode() {
    return this.customStatusCode;
  }

  @Override
  public String getDescription() {
    return this.description;
  }
}