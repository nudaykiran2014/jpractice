package spring_learning.lesson03_dependency_injection.solution;

/**
 * Step 3: The Service that RECEIVES its dependency
 * 
 * ✅ GOOD: This class doesn't create its own MessageSender
 * ✅ GOOD: It receives (gets "injected with") a MessageSender
 * ✅ GOOD: It works with ANY MessageSender - Email, SMS, Push, etc.
 * 
 * This is DEPENDENCY INJECTION:
 * - The "dependency" (MessageSender) is "injected" from outside
 * - The class doesn't care HOW messages are sent
 * - It just knows it has something that CAN send messages
 */
public class NotificationService {
    
    // The dependency - notice it's the INTERFACE, not a specific class
    private MessageSender messageSender;
    
    // CONSTRUCTOR INJECTION - the most common way in Spring
    // The dependency is provided when creating the object
    public NotificationService(MessageSender messageSender) {
        this.messageSender = messageSender;
    }
    
    public void notifyUser(String userId, String message) {
        System.out.println("Notifying user: " + userId);
        messageSender.send(message);
    }
    
    // SETTER INJECTION - another way (less common)
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }
}
