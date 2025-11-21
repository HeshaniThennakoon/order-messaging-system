package com.example.demo.runner;

import com.assignment.avro.Order;
import com.example.demo.kafka.producer.OrderProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OrderGeneratorRunner implements CommandLineRunner {

    private final OrderProducer producer;

    @Override
    public void run(String... args) throws Exception {
        Random r = new Random();

        while (true) {
            Order order = Order.newBuilder()
                    .setOrderId(UUID.randomUUID().toString())
                    .setProduct("Product-" + r.nextInt(5))
                    .setPrice(1 + r.nextDouble(200))
                    .build();

            producer.send(order);
            Thread.sleep(500);
        }
    }
}
