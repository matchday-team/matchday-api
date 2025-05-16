package com.matchday.matchdayserver.config;

import com.matchday.matchdayserver.common.Constants;
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
@EnableWebSecurity(debug = true)//debug 옵션 dev환경에서만 사용
public class SecurityConfig {
    @Bean
    public SecurityFilterChain security(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
            .cors(cors -> cors.configurationSource(myConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable) //csrf disable
            .httpBasic(AbstractHttpConfigurer::disable)//Basic인증 disable
            .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//세션 disable
            //특정 url패턴에 대해서는 인증처리 제외하고 나머지 url (anyRequest()) 에 대해선 인증처리
            .authorizeHttpRequests(a->a.requestMatchers("/member/create","/member/doLogin","/member/google/doLogin","/member/kakao/doLogin").permitAll().anyRequest().authenticated())
            .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public CorsConfigurationSource myConfigurationSource() {
        CorsConfiguration configuration=new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
            Constants.FRONTEND_LOCAL_URL,
            Constants.FRONTEND_PRODUCTION_URL,
            "https://www.matchday-planner.com" // 운영 도메인
            //필요시 추가
        ));
        configuration.setAllowedMethods(Arrays.asList("*")); //모든 HTTP메서드 허용
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type"
            //프론트에서 커스텀 헤더 추가시 설정 추가
        ));
        configuration.setAllowCredentials(true); //인가헤더 사용 허용
        UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
        //서버의 모든 API 엔드포인트에 대해 CORS 정책을 적용 (**는 하위 디렉터리까지 전부 포함)
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
