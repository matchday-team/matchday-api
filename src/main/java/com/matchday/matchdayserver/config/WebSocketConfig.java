package com.matchday.matchdayserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.matchday.matchdayserver.common.Constants;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns(
                    Constants.FRONTEND_LOCAL_URL,
                    Constants.FRONTEND_LOCAL_HTTPS_URL,
                    Constants.FRONTEND_DEV_URL,
                    Constants.FRONTEND_BRANCH_DEPLOY_URL,
                    Constants.FRONTEND_PRODUCTION_URL,
                    Constants.FRONTEND_LOCAL_URL_OLD,
                    Constants.FRONTEND_PRODUCTION_URL_OLD)
                .withSockJS();
    }
}