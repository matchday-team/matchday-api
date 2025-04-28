package com.matchday.matchdayserver.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 웹소켓 메시지 타입
 * 토큰을 담고 있는 기본적인 타입
 * 
 * @param <T> 메시지 타입
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Message<T> {
  @Schema(description = "사용자 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
  private String token;

  @Schema(description = "메시지 타입", example = "success")
  private T data;
}