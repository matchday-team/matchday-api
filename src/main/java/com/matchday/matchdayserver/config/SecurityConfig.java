package com.matchday.matchdayserver.config;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

@Configuration
@EnableWebSecurity(debug = true)//dev환경에서만 사용
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfig corsConfig;

    @Bean
    public SecurityFilterChain security(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(cors -> cors.configurationSource(corsConfig.myConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable) //csrf disable
                .httpBasic(AbstractHttpConfigurer::disable)//Basic인증 disable
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//세션 disable
                //특정 url패턴에 대해서는 인증처리 제외하고 나머지 url (anyRequest()) 에 대해선 인증처리
                .authorizeHttpRequests(a -> a.requestMatchers("/member/create", "/member/doLogin", "/member/google/doLogin", "/member/kakao/doLogin").permitAll().anyRequest().authenticated())
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
