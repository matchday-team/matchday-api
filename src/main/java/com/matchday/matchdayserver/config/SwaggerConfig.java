package com.matchday.matchdayserver.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import java.util.List;

@Configuration    // 스프링 실행시 설정파일 읽기 위한 어노테이션
public class SwaggerConfig {

    @Value("${springdoc.api-docs.url}")
    private String serverUrl;

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI openAPI() {
        Server server = new Server();
        server.setUrl(serverUrl); // API 서버 주소 지정

        return new OpenAPI()
            .info(apiInfo())
            .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME)) // 전역 인증 설정
            .components(new Components()
                .addSecuritySchemes(SECURITY_SCHEME_NAME,
                    new SecurityScheme()
                        .name(SECURITY_SCHEME_NAME)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT") // 선택 사항
                ))
            .servers(List.of(server));
    }

    private Info apiInfo() {
        return new Info()
            .title("MatchDay Swagger")
            .description("개발용 Swagger API 문서")
            .version("0.0.1");
    }
}