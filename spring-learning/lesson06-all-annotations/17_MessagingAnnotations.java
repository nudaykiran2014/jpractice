package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 17: MESSAGING ANNOTATIONS (Kafka, RabbitMQ, JMS)                       ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Spring Boot supports various messaging systems for async communication.
 */
public class _17_MessagingAnnotations {
    public static void main(String[] args) {
        System.out.println("=== MESSAGING ANNOTATIONS ===\n");
        System.out.println("KAFKA:");
        System.out.println("  @EnableKafka        → Enable Kafka");
        System.out.println("  @KafkaListener      → Listen to Kafka topic");
        System.out.println("  @SendTo             → Send response to topic");
        System.out.println("\nRABBITMQ:");
        System.out.println("  @EnableRabbit       → Enable RabbitMQ");
        System.out.println("  @RabbitListener     → Listen to queue");
        System.out.println("\nJMS:");
        System.out.println("  @EnableJms          → Enable JMS");
        System.out.println("  @JmsListener        → Listen to JMS queue");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * KAFKA ANNOTATIONS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Requires: spring-kafka dependency
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     @EnableKafka
 *     public class KafkaConfig { }
 *     
 *     @Component
 *     public class KafkaConsumer {
 *         
 *         // Basic listener
 *         @KafkaListener(topics = "orders", groupId = "order-group")
 *         public void listen(String message) {
 *             System.out.println("Received: " + message);
 *         }
 *         
 *         // With deserialization to object
 *         @KafkaListener(topics = "orders", groupId = "order-group")
 *         public void listenOrder(Order order) {
 *             System.out.println("Order: " + order.getId());
 *         }
 *         
 *         // Multiple topics
 *         @KafkaListener(topics = {"orders", "payments"}, groupId = "main-group")
 *         public void listenMultiple(String message) { }
 *         
 *         // With ConsumerRecord for metadata
 *         @KafkaListener(topics = "orders")
 *         public void listenWithMetadata(ConsumerRecord<String, String> record) {
 *             System.out.println("Key: " + record.key());
 *             System.out.println("Partition: " + record.partition());
 *             System.out.println("Offset: " + record.offset());
 *             System.out.println("Value: " + record.value());
 *         }
 *         
 *         // Batch processing
 *         @KafkaListener(topics = "orders", containerFactory = "batchFactory")
 *         public void listenBatch(List<Order> orders) {
 *             orders.forEach(order -> process(order));
 *         }
 *         
 *         // Send response to another topic
 *         @KafkaListener(topics = "requests")
 *         @SendTo("responses")
 *         public String processAndReply(String request) {
 *             return "Processed: " + request;
 *         }
 *     }
 *     
 *     // Producer
 *     @Service
 *     public class KafkaProducer {
 *         
 *         @Autowired
 *         private KafkaTemplate<String, String> kafkaTemplate;
 *         
 *         public void send(String message) {
 *             kafkaTemplate.send("orders", message);
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * RABBITMQ ANNOTATIONS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Requires: spring-boot-starter-amqp
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     @EnableRabbit
 *     public class RabbitConfig {
 *         
 *         @Bean
 *         public Queue ordersQueue() {
 *             return new Queue("orders-queue", true);
 *         }
 *     }
 *     
 *     @Component
 *     public class RabbitConsumer {
 *         
 *         // Basic listener
 *         @RabbitListener(queues = "orders-queue")
 *         public void listen(String message) {
 *             System.out.println("Received: " + message);
 *         }
 *         
 *         // With object conversion
 *         @RabbitListener(queues = "orders-queue")
 *         public void listenOrder(Order order) {
 *             System.out.println("Order: " + order.getId());
 *         }
 *         
 *         // With Message object for headers
 *         @RabbitListener(queues = "orders-queue")
 *         public void listenWithHeaders(Message message) {
 *             String body = new String(message.getBody());
 *             String correlationId = message.getMessageProperties().getCorrelationId();
 *         }
 *         
 *         // Reply to sender
 *         @RabbitListener(queues = "requests-queue")
 *         @SendTo("responses-queue")
 *         public String processAndReply(String request) {
 *             return "Response: " + request;
 *         }
 *     }
 *     
 *     // Producer
 *     @Service
 *     public class RabbitProducer {
 *         
 *         @Autowired
 *         private RabbitTemplate rabbitTemplate;
 *         
 *         public void send(String message) {
 *             rabbitTemplate.convertAndSend("orders-queue", message);
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * JMS ANNOTATIONS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Requires: spring-boot-starter-activemq (or other JMS provider)
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     @EnableJms
 *     public class JmsConfig { }
 *     
 *     @Component
 *     public class JmsConsumer {
 *         
 *         @JmsListener(destination = "orders-queue")
 *         public void listen(String message) {
 *             System.out.println("Received: " + message);
 *         }
 *         
 *         // With selector (filter messages)
 *         @JmsListener(
 *             destination = "orders-queue",
 *             selector = "orderType = 'PREMIUM'"
 *         )
 *         public void listenPremium(Order order) { }
 *         
 *         // Reply
 *         @JmsListener(destination = "requests")
 *         @SendTo("responses")
 *         public String process(String request) {
 *             return "Done: " + request;
 *         }
 *     }
 *     
 *     // Producer
 *     @Service
 *     public class JmsProducer {
 *         
 *         @Autowired
 *         private JmsTemplate jmsTemplate;
 *         
 *         public void send(String message) {
 *             jmsTemplate.convertAndSend("orders-queue", message);
 *         }
 *     }
 * 
 * MESSAGING COMPARISON:
 * ----------------------
 * | Feature      | Kafka           | RabbitMQ        | JMS             |
 * |--------------|-----------------|-----------------|-----------------|
 * | Model        | Pub/Sub + Log   | Queue/Exchange  | Queue/Topic     |
 * | Ordering     | Per partition   | Per queue       | Per queue       |
 * | Replay       | Yes             | No              | No              |
 * | Use Case     | Event streaming | Task queues     | Enterprise msg  |
 */
