package com.matchday.matchdayserver.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Configuration
@Profile("local")
public class SwaggerConfigLocal {

    @Bean
    public OpenAPI openAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");

        return new OpenAPI()
            .components(new Components())
            .info(apiInfo())
            .servers(List.of(localServer));
    }

    private Info apiInfo() {
        return new Info()
            .title("MatchDay Swagger (Local)")
            .description("로컬 개발용 Swagger API 문서")
            .version("0.0.1");
    }
}