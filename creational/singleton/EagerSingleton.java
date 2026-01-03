package creational.singleton;

public class EagerSingleton {
    private static final EagerSingleton instance = new EagerSingleton();
    
    private EagerSingleton() {
        System.out.println("EagerSingleton instance created");
    }
    
    public static EagerSingleton getInstance() {
        return instance;
    }
    
    public void showMessage() {
        System.out.println("Hello from Eager Singleton!");
    }
}
