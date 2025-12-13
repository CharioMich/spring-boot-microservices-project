package com.babis.microservices.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration(proxyBeanMethods = false)	// Marks this class as test-only configuration. proxyBeanMethods = false improves startup performance by avoiding CGLIB proxies
public class TestOrderServiceApplication {

	public static void main(String[] args) {

		// For many test classes and when all tests share the same infrastructure we create a centralized setup
		// and start the sql container here. This avoids starting/stopping the container for each test class separately which is time consuming.
//		@Bean
//		@ServiceConnection
//		MySQLContainer<?> mysqlContainer() {
//			return new MySQLContainer<>(DockerImageName.parse("mysql:latest"));
//		}
//
//		public static void main(String[] args) {
//			SpringApplication.from(OrderServiceApplication::main).with(TestOrderServiceApplication.class).run(args);
//		}

		SpringApplication.from(OrderServiceApplication::main).run(args);
	}

}
