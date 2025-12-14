package com.babis.microservices.order.service;

import com.babis.microservices.order.client.InventoryClient;
import com.babis.microservices.order.dto.OrderRequest;
import com.babis.microservices.order.model.Order;
import com.babis.microservices.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public void placeOrder(OrderRequest orderRequest) {

        boolean inStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());

        if (inStock) {
            // Map order request to order entity
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setSkuCode(orderRequest.skuCode());
            order.setPrice(orderRequest.price());
            order.setQuantity(orderRequest.quantity());
            // Save order entity to the database
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product is not in stock, skuCode: " + orderRequest.skuCode());
        }
    }
}
