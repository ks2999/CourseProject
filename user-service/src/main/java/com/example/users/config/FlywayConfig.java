package com.example.users.config;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class FlywayConfig {
    
    /**
     * Отключает Flyway для dev профиля
     * В dev профиле таблицы создаются через Hibernate (ddl-auto: create-drop)
     */
    @Bean
    @Profile("dev")
    public FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> {
            // Не выполняем миграции в dev профиле
        };
    }
}

