package com.lhv.account.infrastructure;

import org.testcontainers.containers.MySQLContainer;

public class TestContainerConfig {

    public static final MySQLContainer<?> MYSQL_CONTAINER;

    static {
        MYSQL_CONTAINER = new MySQLContainer<>("mysql:8.0")
                .withDatabaseName("testdb")
                .withUsername("testuser")
                .withPassword("testpass");
        MYSQL_CONTAINER.start();
    }

    private TestContainerConfig() {
    }
}
