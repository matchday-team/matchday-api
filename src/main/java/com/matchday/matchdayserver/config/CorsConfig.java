package com.matchday.matchdayserver.config;

import com.matchday.matchdayserver.common.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource myConfigurationSource() {
        CorsConfiguration configuration=new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
            Constants.FRONTEND_LOCAL_URL,
            Constants.BACKEND_LOCAL_URL,
            Constants.FRONTEND_PRODUCTION_URL,
            Constants.BACKEND_PRODUCTION_URL,
            Constants.FRONTEND_LOCAL_VUE_URL
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
