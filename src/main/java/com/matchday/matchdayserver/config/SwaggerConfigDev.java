package com.matchday.matchdayserver.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import java.util.List;

@Configuration    // 스프링 실행시 설정파일 읽기 위한 어노테이션
@Profile("dev")
public class SwaggerConfigDev {

    @Bean
    public OpenAPI openAPI() {
        Server devServer = new Server();
        devServer.setUrl("https://dev-api.matchday-planner.com"); // HTTPS 주소 명시

        return new OpenAPI()
            .components(new Components())
            .info(apiInfo())
            .servers(List.of(devServer)); // 서버 명시
    }

    private Info apiInfo() {
        return new Info()
            .title("MatchDay Swagger")
            .description("개발용 Swagger API 문서")
            .version("0.0.1");
    }
}