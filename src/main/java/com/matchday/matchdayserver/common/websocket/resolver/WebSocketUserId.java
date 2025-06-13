package com.matchday.matchdayserver.common.websocket.resolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 웹소켓 메시지 핸들러에서 사용자 ID를 자동으로 주입하는 어노테이션
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface WebSocketUserId {
}
