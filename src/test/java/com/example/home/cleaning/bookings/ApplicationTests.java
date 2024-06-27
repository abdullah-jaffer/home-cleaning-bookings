package com.example.home.cleaning.bookings;

import com.example.home.cleaning.bookings.runner.PostgresRunner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.testcontainers.utility.TestcontainersConfiguration;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@ExtendWith(PostgresRunner.class)
class ApplicationTests {

	@Test
	void contextLoads() {
	}

}
