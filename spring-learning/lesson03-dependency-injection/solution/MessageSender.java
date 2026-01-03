package spring_learning.lesson03_dependency_injection.solution;

/**
 * LESSON 3B: THE SOLUTION - Dependency Injection
 * 
 * Step 1: Create an INTERFACE (a contract)
 * 
 * Think of it like a power outlet:
 * - Any device that fits the outlet can be plugged in
 * - The wall doesn't care if it's a lamp, TV, or phone charger
 * - It just provides power to whatever is plugged in
 * 
 * This interface is our "outlet" - any message sender can plug in!
 */
public interface MessageSender {
    void send(String message);
}
