package com.matchday.matchdayserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.matchday.matchdayserver.common.Constants;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins(
            Constants.FRONTEND_LOCAL_URL,
            Constants.FRONTEND_DEV_URL,
            Constants.FRONTEND_PRODUCTION_URL,
            Constants.BACKEND_PRODUCTION_URL,
            Constants.BACKEND_LOCAL_URL,
            Constants.FRONTEND_LOCAL_URL_OLD,
            Constants.FRONTEND_PRODUCTION_URL_OLD
        )
        .allowedMethods("*")
        .allowedHeaders("*");
  }
}
