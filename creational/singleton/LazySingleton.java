package creational.singleton;

public class LazySingleton {
    private static LazySingleton instance;
    
    private LazySingleton() {
        System.out.println("LazySingleton instance created");
    }
    
    public static LazySingleton getInstance() {
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }
    
    public void showMessage() {
        System.out.println("Hello from Lazy Singleton!");
    }
}
