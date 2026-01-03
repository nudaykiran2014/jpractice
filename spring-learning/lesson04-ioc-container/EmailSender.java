package spring_learning.lesson04_ioc_container;

/**
 * Email implementation of MessageSender
 * In real Spring: @Component or @Service
 */
public class EmailSender implements MessageSender {
    
    public EmailSender() {
        System.out.println("   🔨 EmailSender instance created!");
    }
    
    @Override
    public void send(String message) {
        System.out.println("   📧 EMAIL: " + message);
    }
}
