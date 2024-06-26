package com.example.home.cleaning.bookings.runner;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.UUID;

public class PostgresRunner implements BeforeAllCallback {

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:16.1");
        container = container.withCreateContainerCmdModifier(cmd -> cmd.withName("TC_PostgreSQL_" + UUID.randomUUID()));
        container.start();

        System.setProperty("DB_WRITER_CONNECTION_STRING", container.getJdbcUrl());
        System.setProperty("DB_SERVICE_USER_NAME", container.getUsername());
        System.setProperty("DB_SERVICE_USER_PASSWORD", container.getPassword());
        System.setProperty("DB_MIGRATION_USER_NAME", container.getUsername());
        System.setProperty("DB_MIGRATION_USER_PASSWORD", container.getPassword());
    }
}