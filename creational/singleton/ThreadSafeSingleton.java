package creational.singleton;

public class ThreadSafeSingleton {
    private static ThreadSafeSingleton instance;
    
    private ThreadSafeSingleton() {
        System.out.println("ThreadSafeSingleton instance created");
    }
    
    public static synchronized ThreadSafeSingleton getInstance() {
        if (instance == null) {
            instance = new ThreadSafeSingleton();
        }
        return instance;
    }
    
    public void showMessage() {
        System.out.println("Hello from Thread-Safe Singleton!");
    }
}
