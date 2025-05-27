package com.matchday.matchdayserver.config;

import com.matchday.matchdayserver.common.resolver.UserIdResolver;
import com.matchday.matchdayserver.common.resolver.UserSessionResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.matchday.matchdayserver.common.Constants;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final UserIdResolver userIdResolver;
    private final UserSessionResolver userSessionResolver;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins(Constants.FRONTEND_LOCAL_URL, Constants.FRONTEND_PRODUCTION_URL,Constants.BACKEND_PRODUCTION_URL,Constants.BACKEND_LOCAL_URL)
        .allowedMethods("*")
        .allowedHeaders("*");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userIdResolver);
        resolvers.add(userSessionResolver);
    }
}
