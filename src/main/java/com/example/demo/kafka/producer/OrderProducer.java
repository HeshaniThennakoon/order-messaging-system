package com.example.demo.kafka.producer;

import com.assignment.avro.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderProducer {

    private final KafkaTemplate<String, Order> kafkaTemplate;

    public void send(Order order) {
        // Send entire Order object as value
        kafkaTemplate.send("orders", order.getOrderId().toString(), order);
        System.out.println("Produced: " + order);
    }
}
