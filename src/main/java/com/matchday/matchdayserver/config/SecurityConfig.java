package com.matchday.matchdayserver.config;

import com.matchday.matchdayserver.common.auth.CustomAccessDeniedHandler;
import com.matchday.matchdayserver.common.auth.CustomAuthenticationEntryPoint;
import com.matchday.matchdayserver.common.auth.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.util.List;

@Configuration
@EnableWebSecurity(debug = true)//dev환경에서만 사용
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfig corsConfig;
    private final JwtTokenFilter jwtTokenFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final List<String> EXCEPTION = List.of("/open-api/**");
    private final List<String> SWAGGER = List.of("/swagger-ui.html", "/swagger-ui/**", "/swagger-resources/**", "/v2/api-docs", "/v3/api-docs/**");

    @Bean
    public SecurityFilterChain security(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .cors(cors -> cors.configurationSource(corsConfig.myConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable) //csrf disable
            .httpBasic(AbstractHttpConfigurer::disable)//Basic인증 disable
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//세션 disable
            //특정 url패턴에 대해서는 인증처리 제외하고 나머지 url (anyRequest()) 에 대해선 인증처리
            .authorizeHttpRequests(req -> {
                req
                    //필요 : X
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                    .permitAll()
                    .requestMatchers(SWAGGER.toArray(new String[0]))
                    .permitAll()
                    .requestMatchers(EXCEPTION.toArray(new String[0]))
                    //필요 : 인증,권한
                    .permitAll()
                    .requestMatchers("/api/**")
                    .hasAnyRole("USER")
                    //필요 : 권한
                    .anyRequest()
                    .authenticated();
            });

        //예외 발생 시 바로 오류 출력
        httpSecurity.exceptionHandling(
            e -> e.authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
        );

        httpSecurity.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }
}