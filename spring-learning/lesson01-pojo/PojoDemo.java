package spring_learning.lesson01_pojo;

/**
 * POJO Demo - See how simple Java objects work
 * 
 * Run this to understand POJOs before we dive into Spring!
 */
public class PojoDemo {
    
    public static void main(String[] args) {
        System.out.println("=== LESSON 1: Understanding POJOs ===\n");
        
        // Creating a POJO using default constructor + setters
        System.out.println("1. Creating Person using setters:");
        Person person1 = new Person();
        person1.setName("Alice");
        person1.setAge(25);
        person1.setEmail("alice@example.com");
        System.out.println("   " + person1);
        
        // Creating a POJO using parameterized constructor
        System.out.println("\n2. Creating Person using constructor:");
        Person person2 = new Person("Bob", 30, "bob@example.com");
        System.out.println("   " + person2);
        
        // Reading data using getters
        System.out.println("\n3. Reading data using getters:");
        System.out.println("   Name: " + person2.getName());
        System.out.println("   Age: " + person2.getAge());
        System.out.println("   Email: " + person2.getEmail());
        
        System.out.println("\n=== KEY TAKEAWAY ===");
        System.out.println("A POJO is just a simple class with:");
        System.out.println("  - Private fields");
        System.out.println("  - Public getters/setters");
        System.out.println("  - No framework dependencies!");
        System.out.println("\nSpring Framework is built around POJOs.");
        System.out.println("You write simple classes, Spring adds the magic!");
    }
}
