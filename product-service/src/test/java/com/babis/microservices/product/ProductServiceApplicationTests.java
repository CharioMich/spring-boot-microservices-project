package com.babis.microservices.product;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.hamcrest.Matchers;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;


/**
 * Integration tests for the Product Service application.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

	/**
	 * Because of the @ServiceConnection annotation, Spring Boot will automatically configure the application to use this MongoDB container.
	 * It will: detect the MongoDBContainer bean, start the container, extracts its connection details (host, port, credentials)
	 * and automatically set the mongodb uri
	 */
	@Container
	@ServiceConnection
	static MongoDBContainer mongoDbContainer = new MongoDBContainer("mongo:7.0.5");

	// Because we are running the tests with a random port, we need to inject the port number
	@LocalServerPort
	private Integer port;

	// Configure RestAssured before each test
	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	static {
		mongoDbContainer.start();
	}

	@Test
	void shouldCreateProduct() {
		String requestBody = """
				{
				     "name": "Iphone 15",
				     "description": "Smartphone from Apple",
				     "price": 750
				 }
				""";

		RestAssured.given()
				.contentType("application/json")
				.body(requestBody)
				.when()
				.post("/api/products")
				.then()
				.statusCode(201)
				.body("id", Matchers.notNullValue())
				.body("name", Matchers.equalTo("Iphone 15"))
				.body("description", Matchers.equalTo("Smartphone from Apple"))
				.body("price", Matchers.equalTo(750));
	}

}
