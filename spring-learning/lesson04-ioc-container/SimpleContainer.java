package spring_learning.lesson04_ioc_container;

import java.util.HashMap;
import java.util.Map;

/**
 * A SIMPLE IoC Container that mimics what Spring does
 * 
 * This is a teaching example - real Spring is much more powerful!
 * 
 * What this container does:
 * 1. Stores class registrations (what beans exist)
 * 2. Creates instances when requested
 * 3. Caches instances (singleton behavior)
 */
public class SimpleContainer {
    
    // Registry: bean name → class type
    private Map<String, Class<?>> registry = new HashMap<>();
    
    // Cache: bean name → instance (singleton pattern)
    private Map<String, Object> singletons = new HashMap<>();
    
    /**
     * Register a class with the container
     * Like adding an item to the warehouse inventory
     */
    public void register(String beanName, Class<?> beanClass) {
        registry.put(beanName, beanClass);
        System.out.println("   → Registered bean: " + beanName + " (" + beanClass.getSimpleName() + ")");
    }
    
    /**
     * Get a bean from the container
     * Container creates it if needed, or returns cached instance
     */
    public Object getBean(String beanName) {
        // Check if already created (singleton)
        if (singletons.containsKey(beanName)) {
            return singletons.get(beanName);
        }
        
        // Get the class from registry
        Class<?> beanClass = registry.get(beanName);
        if (beanClass == null) {
            throw new RuntimeException("Bean not found: " + beanName);
        }
        
        try {
            // Create new instance
            Object instance = beanClass.getDeclaredConstructor().newInstance();
            
            // Cache it (singleton)
            singletons.put(beanName, instance);
            
            return instance;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create bean: " + beanName, e);
        }
    }
}
