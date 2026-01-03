package spring_learning.lesson04_ioc_container;

/**
 * SMS implementation of MessageSender
 */
public class SmsSender implements MessageSender {
    
    public SmsSender() {
        System.out.println("   🔨 SmsSender instance created!");
    }
    
    @Override
    public void send(String message) {
        System.out.println("   📱 SMS: " + message);
    }
}
