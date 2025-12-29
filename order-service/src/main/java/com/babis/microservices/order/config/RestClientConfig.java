package com.babis.microservices.order.config;

import com.babis.microservices.order.client.InventoryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Value("${inventory.url}")
    private String inventoryServiceBaseUrl;

    /**
     * Creating the InventoryClient Bean, a runtime implementation of InventoryClient interface
     * using Spring's HTTP Interface Proxy mechanism.
     * Proxy -> An object that pretends to be another object and intercepts method calls.
     */
    @Bean
    public InventoryClient inventoryClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(inventoryServiceBaseUrl)
                .build();

        // Adapting RestClient to Spring’s Proxy System | bridge RestClient with the HTTP Interface Proxy
        // Spring’s proxy system is client-agnostic, so we need to adapt RestClient to work with it. We could use
        // other HTTP clients as well, like WebClient or Apache HttpClient, by using their respective adapters.
        var restClientAdapter = RestClientAdapter.create(restClient);

        // the HttpServiceProxyFactory is responsible for creating proxy instances of HTTP service interfaces.
        // It uses the provided RestClientAdapter to handle the actual HTTP communication.
        var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();

        // Creates a class at runtime that implements the InventoryClient interface
        return httpServiceProxyFactory.createClient(InventoryClient.class);
    }

}
