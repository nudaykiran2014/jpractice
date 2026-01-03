package spring_learning.lesson03_dependency_injection.solution;

/**
 * SmsSender - another implementation that "plugs into" MessageSender
 */
public class SmsSender implements MessageSender {
    
    @Override
    public void send(String message) {
        System.out.println("📱 SMS: " + message);
    }
}
