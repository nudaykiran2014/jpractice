package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 33: SPRING INTEGRATION ANNOTATIONS                                     ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Spring Integration provides messaging patterns for enterprise integration.
 * 
 * Requires: spring-boot-starter-integration
 */
public class _33_SpringIntegrationAnnotations {
    public static void main(String[] args) {
        System.out.println("=== SPRING INTEGRATION ANNOTATIONS ===\n");
        System.out.println("@EnableIntegration   → Enable Spring Integration");
        System.out.println("@MessagingGateway    → Define messaging gateway");
        System.out.println("@ServiceActivator    → Message handler");
        System.out.println("@Transformer         → Transform messages");
        System.out.println("@Filter              → Filter messages");
        System.out.println("@Router              → Route messages");
        System.out.println("@Splitter            → Split messages");
        System.out.println("@Aggregator          → Aggregate messages");
        System.out.println("@InboundChannelAdapter → Receive from external");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * SPRING INTEGRATION CONCEPTS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Message      → Data + Headers
 * Channel      → Pipe between components
 * Endpoint     → Component that processes messages
 * Gateway      → Entry point to messaging system
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @MessagingGateway
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Define entry point to send messages
 * 
 * EXAMPLE:
 * ---------
 *     @MessagingGateway(defaultRequestChannel = "inputChannel")
 *     public interface OrderGateway {
 *         
 *         void processOrder(Order order);
 *         
 *         @Gateway(requestChannel = "priorityChannel")
 *         void processPriorityOrder(Order order);
 *         
 *         // With reply
 *         @Gateway(requestChannel = "inputChannel", replyChannel = "outputChannel")
 *         OrderResult processAndWait(Order order);
 *     }
 *     
 *     // Usage
 *     @Service
 *     public class OrderService {
 *         
 *         @Autowired
 *         private OrderGateway orderGateway;
 *         
 *         public void submitOrder(Order order) {
 *             orderGateway.processOrder(order);
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @ServiceActivator
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Handle messages from a channel
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     @EnableIntegration
 *     public class IntegrationConfig {
 *         
 *         @Bean
 *         public MessageChannel inputChannel() {
 *             return new DirectChannel();
 *         }
 *         
 *         @Bean
 *         public MessageChannel outputChannel() {
 *             return new DirectChannel();
 *         }
 *     }
 *     
 *     @Component
 *     public class OrderHandler {
 *         
 *         @ServiceActivator(inputChannel = "inputChannel", outputChannel = "outputChannel")
 *         public OrderResult handleOrder(Order order) {
 *             // Process order
 *             return new OrderResult(order.getId(), "PROCESSED");
 *         }
 *         
 *         // With Message wrapper
 *         @ServiceActivator(inputChannel = "inputChannel")
 *         public void handleMessage(Message<Order> message) {
 *             Order order = message.getPayload();
 *             String correlationId = message.getHeaders().get("correlationId", String.class);
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Transformer
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Transform message payload
 * 
 * EXAMPLE:
 * ---------
 *     @Component
 *     public class MessageTransformers {
 *         
 *         @Transformer(inputChannel = "rawChannel", outputChannel = "processedChannel")
 *         public OrderDTO transformOrder(Order order) {
 *             return new OrderDTO(order.getId(), order.getTotal());
 *         }
 *         
 *         @Transformer(inputChannel = "jsonChannel", outputChannel = "objectChannel")
 *         public Order jsonToOrder(String json) {
 *             return objectMapper.readValue(json, Order.class);
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Filter
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Filter messages (only pass matching ones)
 * 
 * EXAMPLE:
 * ---------
 *     @Component
 *     public class MessageFilters {
 *         
 *         @Filter(inputChannel = "inputChannel", outputChannel = "validChannel")
 *         public boolean filterValidOrders(Order order) {
 *             return order.getTotal() > 0 && order.getItems() != null;
 *         }
 *         
 *         // With discard channel
 *         @Filter(inputChannel = "inputChannel", 
 *                 outputChannel = "validChannel",
 *                 discardChannel = "invalidChannel")
 *         public boolean filterLargeOrders(Order order) {
 *             return order.getTotal() >= 100;
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Router
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Route messages to different channels
 * 
 * EXAMPLE:
 * ---------
 *     @Component
 *     public class OrderRouter {
 *         
 *         @Router(inputChannel = "orderChannel")
 *         public String routeOrder(Order order) {
 *             // Return channel name based on condition
 *             if (order.isPriority()) {
 *                 return "priorityChannel";
 *             } else if (order.getTotal() > 1000) {
 *                 return "largeOrderChannel";
 *             } else {
 *                 return "standardChannel";
 *             }
 *         }
 *         
 *         // Route to multiple channels
 *         @Router(inputChannel = "orderChannel")
 *         public List<String> routeToMultiple(Order order) {
 *             List<String> channels = new ArrayList<>();
 *             channels.add("auditChannel");
 *             if (order.isPriority()) {
 *                 channels.add("priorityChannel");
 *             }
 *             return channels;
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Splitter and @Aggregator
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Split one message into many, then aggregate back
 * 
 * EXAMPLE:
 * ---------
 *     @Component
 *     public class OrderProcessor {
 *         
 *         // Split order into individual items
 *         @Splitter(inputChannel = "orderChannel", outputChannel = "itemChannel")
 *         public List<OrderItem> splitOrder(Order order) {
 *             return order.getItems();
 *         }
 *         
 *         // Process each item
 *         @ServiceActivator(inputChannel = "itemChannel", outputChannel = "processedItemChannel")
 *         public ProcessedItem processItem(OrderItem item) {
 *             return new ProcessedItem(item, calculatePrice(item));
 *         }
 *         
 *         // Aggregate items back
 *         @Aggregator(inputChannel = "processedItemChannel", outputChannel = "resultChannel")
 *         public OrderResult aggregateResults(List<ProcessedItem> items) {
 *             double total = items.stream()
 *                 .mapToDouble(ProcessedItem::getPrice)
 *                 .sum();
 *             return new OrderResult(total);
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @InboundChannelAdapter (Polling)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Poll external source and create messages
 * 
 * EXAMPLE:
 * ---------
 *     @Component
 *     public class FilePoller {
 *         
 *         @InboundChannelAdapter(
 *             channel = "fileChannel",
 *             poller = @Poller(fixedDelay = "5000")  // Every 5 seconds
 *         )
 *         public File pollDirectory() {
 *             // Return file to process
 *             return new File("/inbox/orders.csv");
 *         }
 *     }
 *     
 *     // File integration example
 *     @Bean
 *     public IntegrationFlow fileFlow() {
 *         return IntegrationFlows
 *             .from(Files.inboundAdapter(new File("/inbox"))
 *                 .patternFilter("*.csv"),
 *                 e -> e.poller(Pollers.fixedDelay(1000)))
 *             .transform(Files.toStringTransformer())
 *             .channel("processChannel")
 *             .get();
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * IntegrationFlow (DSL)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     public class FlowConfig {
 *         
 *         @Bean
 *         public IntegrationFlow orderFlow() {
 *             return IntegrationFlows
 *                 .from("inputChannel")
 *                 .filter(Order.class, o -> o.getTotal() > 0)
 *                 .transform(Order.class, o -> new OrderDTO(o))
 *                 .route(OrderDTO.class, o -> o.isPriority() ? "priority" : "standard",
 *                     r -> r
 *                         .subFlowMapping("priority", sf -> sf.channel("priorityChannel"))
 *                         .subFlowMapping("standard", sf -> sf.channel("standardChannel")))
 *                 .get();
 *         }
 *     }
 * 
 * INTEGRATION PATTERNS:
 * ----------------------
 * | Pattern     | Annotation        | Purpose                  |
 * |-------------|-------------------|--------------------------|
 * | Gateway     | @MessagingGateway | Entry point              |
 * | Handler     | @ServiceActivator | Process messages         |
 * | Transform   | @Transformer      | Convert payload          |
 * | Filter      | @Filter           | Conditional pass         |
 * | Router      | @Router           | Route to channels        |
 * | Splitter    | @Splitter         | Split into multiple      |
 * | Aggregator  | @Aggregator       | Combine messages         |
 */
