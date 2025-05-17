package com.matchday.matchdayserver.config;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

    /**
     * SQL Script에서 오류가 났을 경우
     * SQL 수정 후 자동으로 DB를 수정하는 FlywayMigrationStrategy
     * @return
     */
    @Bean
    public FlywayMigrationStrategy repairMigrationStrategy() {
        return flyway -> {
            flyway.repair();
            flyway.migrate();
        };
    }
}
