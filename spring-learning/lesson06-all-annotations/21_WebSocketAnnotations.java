package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 21: WEBSOCKET ANNOTATIONS                                              ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * WebSockets enable real-time, bidirectional communication.
 * 
 * Requires: spring-boot-starter-websocket
 */
public class _21_WebSocketAnnotations {
    public static void main(String[] args) {
        System.out.println("=== WEBSOCKET ANNOTATIONS ===\n");
        System.out.println("@EnableWebSocketMessageBroker → Enable STOMP messaging");
        System.out.println("@MessageMapping    → Handle WebSocket messages");
        System.out.println("@SendTo            → Send to all subscribers");
        System.out.println("@SendToUser        → Send to specific user");
        System.out.println("@SubscribeMapping  → Handle subscription");
        System.out.println("@Payload           → Message payload");
        System.out.println("@DestinationVariable → Path variable in destination");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * WEBSOCKET CONFIGURATION
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     @EnableWebSocketMessageBroker
 *     public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
 *         
 *         @Override
 *         public void configureMessageBroker(MessageBrokerRegistry config) {
 *             config.enableSimpleBroker("/topic", "/queue");  // Subscribe destinations
 *             config.setApplicationDestinationPrefixes("/app");  // Send destinations
 *             config.setUserDestinationPrefix("/user");  // User-specific messages
 *         }
 *         
 *         @Override
 *         public void registerStompEndpoints(StompEndpointRegistry registry) {
 *             registry.addEndpoint("/ws")  // WebSocket endpoint
 *                 .setAllowedOrigins("*")
 *                 .withSockJS();  // Fallback for older browsers
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @MessageMapping and @SendTo
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Handle incoming WebSocket messages and broadcast responses
 * 
 * EXAMPLE:
 * ---------
 *     @Controller
 *     public class ChatController {
 *         
 *         // Client sends to: /app/chat
 *         // Response goes to: /topic/messages (all subscribers)
 *         @MessageMapping("/chat")
 *         @SendTo("/topic/messages")
 *         public ChatMessage sendMessage(ChatMessage message) {
 *             message.setTimestamp(LocalDateTime.now());
 *             return message;  // Broadcast to all subscribers
 *         }
 *         
 *         // With path variable
 *         // Client sends to: /app/chat/room123
 *         @MessageMapping("/chat/{roomId}")
 *         @SendTo("/topic/rooms/{roomId}")
 *         public ChatMessage sendToRoom(
 *             @DestinationVariable String roomId,
 *             @Payload ChatMessage message
 *         ) {
 *             message.setRoom(roomId);
 *             return message;
 *         }
 *         
 *         // Access message headers
 *         @MessageMapping("/chat")
 *         public void handleWithHeaders(
 *             @Payload ChatMessage message,
 *             @Header("username") String username,
 *             SimpMessageHeaderAccessor headerAccessor
 *         ) {
 *             String sessionId = headerAccessor.getSessionId();
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @SendToUser
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Send message to a specific user only
 * 
 * EXAMPLE:
 * ---------
 *     @Controller
 *     public class NotificationController {
 *         
 *         // Send to specific user's queue
 *         @MessageMapping("/private")
 *         @SendToUser("/queue/notifications")
 *         public Notification sendPrivate(
 *             @Payload NotificationRequest request,
 *             Principal principal
 *         ) {
 *             return new Notification("Private message for " + principal.getName());
 *         }
 *         
 *         // Programmatic sending to user
 *         @Autowired
 *         private SimpMessagingTemplate messagingTemplate;
 *         
 *         public void notifyUser(String username, String message) {
 *             messagingTemplate.convertAndSendToUser(
 *                 username,
 *                 "/queue/notifications",
 *                 new Notification(message)
 *             );
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @SubscribeMapping
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Handle subscription requests and send initial data
 * 
 * EXAMPLE:
 * ---------
 *     @Controller
 *     public class DataController {
 *         
 *         // When client subscribes to /app/initial-data
 *         // Send them the current data immediately
 *         @SubscribeMapping("/initial-data")
 *         public List<Message> getInitialData() {
 *             return messageService.getRecentMessages();
 *         }
 *         
 *         @SubscribeMapping("/user-status")
 *         public UserStatus getUserStatus(Principal principal) {
 *             return userService.getStatus(principal.getName());
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * PROGRAMMATIC MESSAGING
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @Service
 *     public class NotificationService {
 *         
 *         @Autowired
 *         private SimpMessagingTemplate template;
 *         
 *         // Send to all subscribers of a topic
 *         public void broadcastMessage(String message) {
 *             template.convertAndSend("/topic/news", message);
 *         }
 *         
 *         // Send to specific user
 *         public void sendToUser(String username, Notification notification) {
 *             template.convertAndSendToUser(
 *                 username,
 *                 "/queue/notifications",
 *                 notification
 *             );
 *         }
 *         
 *         // Send to specific room
 *         public void sendToRoom(String roomId, ChatMessage message) {
 *             template.convertAndSend("/topic/rooms/" + roomId, message);
 *         }
 *     }
 * 
 * CLIENT JAVASCRIPT:
 * -------------------
 *     const socket = new SockJS('/ws');
 *     const stompClient = Stomp.over(socket);
 *     
 *     stompClient.connect({}, () => {
 *         // Subscribe to topic
 *         stompClient.subscribe('/topic/messages', (message) => {
 *             console.log(JSON.parse(message.body));
 *         });
 *         
 *         // Send message
 *         stompClient.send('/app/chat', {}, JSON.stringify({
 *             content: 'Hello!'
 *         }));
 *     });
 */
