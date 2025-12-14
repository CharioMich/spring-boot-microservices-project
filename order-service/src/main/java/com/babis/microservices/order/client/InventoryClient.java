package com.babis.microservices.order.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class InventoryClient {

    private final RestClient restClient;

    public boolean isInStock(String skuCode, Integer quantity) {
        Boolean response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/inventory")
                        .queryParam("skuCode", skuCode)
                        .queryParam("quantity", quantity)
                        .build())
                .retrieve()
                .body(Boolean.class);

        // Match Feign behavior: never return null
        return Boolean.TRUE.equals(response);
    }
}
