package com.babis.microservices.order.event;

public record OrderPlacedEvent(
        String orderNumber,
        String email
) {}
