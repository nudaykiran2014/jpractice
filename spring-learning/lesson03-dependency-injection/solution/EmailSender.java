package spring_learning.lesson03_dependency_injection.solution;

/**
 * Step 2: Create implementations of the interface
 * 
 * EmailSender "plugs into" the MessageSender interface
 */
public class EmailSender implements MessageSender {
    
    @Override
    public void send(String message) {
        System.out.println("📧 EMAIL: " + message);
    }
}
