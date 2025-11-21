package com.example.demo.kafka.consumer;

import com.assignment.avro.Order;
import com.example.demo.exception.PermanentException;
import com.example.demo.exception.TransientException;
import com.example.demo.service.RunningAverageService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderConsumer {

    private final RunningAverageService avgService;
    private final KafkaTemplate<String, Order> kafkaTemplate;

    @KafkaListener(topics = "orders", groupId = "orders-group")
    public void consume(Order order) {

        int retries = 0;

        while (true) {
            try {
                process(order); // simulate business logic

                double avg = avgService.add(order.getPrice());
                System.out.println("Consumed: " + order);
                System.out.println("Running Average: " + avg);
                break;

            } catch (TransientException e) {
                retries++;
                if (retries > 3) {
                    System.out.println("Retry failed → sending to DLQ");
                    kafkaTemplate.send("orders-dlq", order);
                    break;
                }
                System.out.println("Transient error → retrying...");
                try { Thread.sleep(1000 * retries); } catch (Exception ignored) {}
            }
            catch (PermanentException e) {
                System.out.println("Permanent error → DLQ");
                kafkaTemplate.send("orders-dlq", order);
                break;
            }
        }
    }

    private void process(Order order) {
        double r = Math.random();

        if (r < 0.10)
            throw new PermanentException("Bad data");

        if (r < 0.25)
            throw new TransientException("System timeout");
    }
}
