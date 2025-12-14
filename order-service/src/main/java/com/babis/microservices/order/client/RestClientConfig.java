package com.babis.microservices.order.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    RestClient restClient(RestClient.Builder builder,
                          @Value("${inventory.url}") String inventoryUrl) {
        return builder
                .baseUrl(inventoryUrl)
                .build();
    }
}
