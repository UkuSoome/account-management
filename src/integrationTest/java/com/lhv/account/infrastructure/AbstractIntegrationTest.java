package com.lhv.account.infrastructure;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static com.lhv.account.infrastructure.TestContainerConfig.MYSQL_CONTAINER;

@SpringBootTest
@ActiveProfiles("integrationtest")
public abstract class AbstractIntegrationTest {

    static {
        TestContainerConfig.MYSQL_CONTAINER.isRunning();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
    }

}