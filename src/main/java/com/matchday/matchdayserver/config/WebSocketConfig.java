package com.matchday.matchdayserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.matchday.matchdayserver.common.Constants;
import com.matchday.matchdayserver.common.websocket.interceptor.JwtWebSocketChannelInterceptor;
import com.matchday.matchdayserver.common.websocket.interceptor.JwtWebSocketHandshakeInterceptor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtWebSocketHandshakeInterceptor handshakeInterceptor;
    private final JwtWebSocketChannelInterceptor channelInterceptor;

    @Override
    public void configureMessageBroker(@NonNull MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(@NonNull StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
            .setAllowedOriginPatterns(
                Constants.FRONTEND_LOCAL_URL,
                Constants.FRONTEND_LOCAL_HTTPS_URL,
                Constants.FRONTEND_DEV_URL,
                Constants.FRONTEND_BRANCH_DEPLOY_URL,
                Constants.FRONTEND_PRODUCTION_URL,
                Constants.FRONTEND_LOCAL_VUE_URL,
                Constants.FRONTEND_LOCAL_URL_OLD,
                Constants.FRONTEND_PRODUCTION_URL_OLD)
            .addInterceptors(handshakeInterceptor) // JWT 핸드셰이크 인터셉터 추가
            .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(@NonNull ChannelRegistration registration) {
        registration.interceptors(channelInterceptor); // JWT 채널 인터셉터 추가
    }
}