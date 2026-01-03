package spring_learning.lesson03_dependency_injection.solution;

/**
 * PushNotificationSender - yet another implementation!
 * 
 * See how easy it is to add new ways to send messages
 * without changing the NotificationService!
 */
public class PushNotificationSender implements MessageSender {
    
    @Override
    public void send(String message) {
        System.out.println("🔔 PUSH: " + message);
    }
}
