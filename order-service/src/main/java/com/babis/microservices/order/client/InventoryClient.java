package com.babis.microservices.order.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface InventoryClient {

    Logger log = LoggerFactory.getLogger(InventoryClient.class);

    @GetExchange("/api/inventory")
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallBackMethod")
    @Retry(name = "inventory") // Retry mechanism (based on config), if still failing then circuit breaker records failure and fallback method is triggered
    boolean isInStock(@RequestParam String skuCode, @RequestParam Integer quantity);

    /**
     * concrete method inside an interface. Allows for behaviour inside the interface.
     * must be marked as default so an actual method body is provided
     * In order to add logging information, method signature must match original args + Throwable
     */
    default boolean fallBackMethod(String skuCode, Integer quantity, Throwable exception) {
        log.warn("Fallback method triggered for skuCode={}, quantity={} with error={}", skuCode, quantity, exception.getMessage());
        return false;
    }

}
