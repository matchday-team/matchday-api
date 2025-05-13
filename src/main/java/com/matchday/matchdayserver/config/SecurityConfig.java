package com.matchday.matchdayserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity(debug = true)//dev환경에서만 사용
public class SecurityConfig {

}
