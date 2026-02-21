package com.babis.microservices.order.service;

import com.babis.microservices.order.client.InventoryClient;
import com.babis.microservices.order.dto.OrderRequest;
import com.babis.microservices.order.event.OrderPlacedEvent;
import com.babis.microservices.order.model.Order;
import com.babis.microservices.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public String placeOrder(OrderRequest orderRequest) {

        boolean inStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());
//        System.out.println("Order Service - placeOrder -> inStock: " + inStock);

        if (inStock) {

            // Map order request to order entity
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            order.setSkuCode(orderRequest.skuCode());
            order.setPrice(orderRequest.price());
            order.setQuantity(orderRequest.quantity());
            // Save order entity to the database
            orderRepository.save(order);

            // Send message to Kafka topic
            OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();

            orderPlacedEvent.setOrderNumber(order.getOrderNumber());
            orderPlacedEvent.setEmail(orderRequest.userDetails().email());
            orderPlacedEvent.setFirstName(orderRequest.userDetails().firstName());
            orderPlacedEvent.setLastName(orderRequest.userDetails().lastName());

            log.info("Start - Sending OrderPlacedEvent to Kafka topic: {}", orderPlacedEvent);

            kafkaTemplate.send("order-placed-topic", orderPlacedEvent);

            log.info("End - OrderPlacedEvent sent to Kafka topic: {}", orderPlacedEvent);

            return "Order Placed Successfully";

        } else {
            System.out.println("Product is not in stock, skuCode: " + orderRequest.skuCode());
            return "false";
        }
    }
}
