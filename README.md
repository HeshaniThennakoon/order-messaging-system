## 🌟 EC8203: Kafka Orders Processing System (Spring Boot + Avro + Docker + Kafka)

This project demonstrates a real-time stream processing application using Apache Kafka, built with Spring Boot and utilizing Avro for efficient, schema-based message serialization. It fulfills the requirements for EC8203 – Applied Big Data Engineering Assignment (Chapter 3).

-----

## ✨ Key Features

  * Real-time Order Generation: A Spring Boot application acts as a Kafka Producer, generating random order events.
  * Avro Serialization: Messages are serialized and deserialized using Avro, ensuring schema compatibility and compact payload size.
  * Robust Consumer Logic: The Kafka Consumer implements advanced features:
      * Running Average Calculation for real-time analytics.
      * Retry Logic to handle transient (temporary) processing failures.
      * Dead Letter Queue (DLQ) implementation for isolating and managing permanent failures.
  * Dockerized Environment: A full Kafka cluster (Broker and Zookeeper) is managed via Docker Compose for easy setup and reproducibility.

-----

## 📦 Kafka Setup (Docker Compose)

The required Kafka and Zookeeper services are defined in `docker-compose.yml`.

### 🚀 Starting the Cluster

To start the Kafka and Zookeeper containers in detached mode:

```bash
docker-compose up -d
```

### ✅ Verifying Containers

Confirm that the containers are running:

```bash
docker ps
```

You should see containers for Kafka and Zookeeper.

-----

## 🧬 Avro Schema and Class Generation

The project uses the following Avro schema for the `Order` record, defined in `src/main/resources/avro/Order.avsc`:

```agsl
{
  "namespace": "com.assignment.avro",
  "type": "record",
  "name": "Order",
  "fields": [
    {"name": "orderId", "type": "string"},
    {"name": "product", "type": "string"},
    {"name": "price", "type": "double"}
  ]
}
```

### 🛠 Generating Avro Classes

The `avro-maven-plugin` is configured to automatically generate the necessary Java classes from the schema during the build process.

To manually generate the classes (if necessary) or during the compilation phase:

```bash
mvn clean compile
```

-----

## 🔧 Build and Run the Application

### 1\. Install Dependencies and Generate Classes

Run the full build life cycle to install dependencies and ensure Avro classes are generated:

```bash
mvn clean install
```

### 2\. Run the Spring Boot Application

Execute the main application using the Spring Boot plugin:

```bash
mvn spring-boot:run
```

### 3\. Verify Output

Observe the console output. The producer will log orders being sent, and the consumer will log orders received, processing status (including retries/DLQ simulation), and the running average calculation.

```yml
Produced: Order{orderId=..., product=Product-1, price=98.45}
...
Consumed: Order{orderId=..., product=Product-1, price=98.45}
Processing Order [Order{orderId=...}] - Attempt 1
Running Average: 123.45
...
[DLQ] Sending failed message to orders.DLQ
```

-----

## 🛠 How the System Works

The system is split into a Kafka Producer for generating data and a Kafka Consumer for processing it robustly.

### 1\. Kafka Producer (OrderGeneratorRunner + OrderProducer)

  * OrderGeneratorRunner: A Spring Boot `CommandLineRunner` component that periodically triggers the generation of synthetic `Order` events.
  * OrderProducer: Encapsulates the logic for sending messages. It uses Spring Kafka's `KafkaTemplate` configured with Avro serializers to send the message to the Kafka topic `orders` every 500ms.

Example Producer Output:

```agsl
Produced: Order{orderId=1a2b3c4d, product=Product-1, price=98.45}
```

### 2\. Kafka Consumer (OrderConsumer)

The consumer is the core of the assignment, demonstrating resilient message processing.

  * Message Consumption: Listens to the `orders` topic, receiving Avro-deserialized `Order` objects.
  * Running Average: Calculates and updates a running average of the order prices, showcasing a basic stream analytics task.
  * Simulated Failures & Retry Logic:
      * The consumer logic is designed to randomly throw exceptions (transient failures) during processing.
      * Spring-Kafka's Retry mechanism is configured to automatically re-attempt processing of the failed message a defined number of times.
  * Dead Letter Queue (DLQ):
      * If a message fails all configured retry attempts (indicating a permanent or non-recoverable error), it is automatically redirected to a dedicated topic, the Dead Letter Queue (e.g., `orders.DLQ`).
      * This prevents the application from halting on bad messages and allows for manual inspection and reprocessing of failed events.

-----

## ⚙️ Project Structure Highlights

| Component | Description |
| :--- | :--- |
| `Order.avsc` | The Avro schema defining the data structure. |
| `OrderGeneratorRunner.java` | The component that periodically triggers order generation. |
| `OrderProducer.java` | Service responsible for sending Avro-serialized orders to Kafka. |
| `OrderConsumer.java` | Core component with Retry/DLQ logic and Running Average calculation. |
| `docker-compose.yml` | Defines the services for the Kafka and Zookeeper cluster. |

-----