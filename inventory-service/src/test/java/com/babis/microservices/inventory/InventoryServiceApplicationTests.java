package com.babis.microservices.inventory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.mysql.MySQLContainer;

import io.restassured.RestAssured;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InventoryServiceApplicationTests {

	@ServiceConnection
	static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.3.0");

	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	static {
		mySQLContainer.start();
	}

	@Test
	void shouldReturnStock() {
		boolean positiveResponse = Boolean.parseBoolean(
					RestAssured.given()
							.queryParam("skuCode", "iphone_15")
							.queryParam("quantity", 1)
							.when()
							.get("/api/inventory")
							.then()
							.statusCode(200)
							.extract()
							.asString()
				);

		assertTrue(positiveResponse);

		boolean negativeResponse = Boolean.parseBoolean(
					RestAssured.given()
							.queryParam("skuCode", "iphone_15")
							.queryParam("quantity", 1000)
							.when()
							.get("/api/inventory")
							.then()
							.statusCode(200)
							.extract()
							.asString()
				);
		assertFalse(negativeResponse);

	}

}
