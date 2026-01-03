package spring_learning.lesson03_dependency_injection.problem;

/**
 * LESSON 3A: THE PROBLEM - Tight Coupling (WITHOUT Dependency Injection)
 * 
 * Imagine you run a delivery company. You ONLY use motorcycles.
 * What if you want to use cars or bicycles later? You're STUCK!
 * 
 * This code shows the PROBLEM that Spring solves.
 */

// This service sends notifications
// PROBLEM: It creates its own EmailSender - tightly coupled!
public class NotificationServiceOld {
    
    // ❌ BAD: Creating dependency inside the class
    // This is called "tight coupling"
    private EmailSender emailSender = new EmailSender();
    
    public void notifyUser(String message) {
        // Can ONLY send emails - what if we want SMS?
        emailSender.send(message);
    }
}

// Email sender - one way to send messages
class EmailSender {
    public void send(String message) {
        System.out.println("📧 Sending EMAIL: " + message);
    }
}

// SMS sender - another way to send messages  
class SmsSender {
    public void send(String message) {
        System.out.println("📱 Sending SMS: " + message);
    }
}
