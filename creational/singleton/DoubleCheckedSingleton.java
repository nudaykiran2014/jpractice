package creational.singleton;

public class DoubleCheckedSingleton {
    private static volatile DoubleCheckedSingleton instance;
    
    private DoubleCheckedSingleton() {
        System.out.println("DoubleCheckedSingleton instance created");
    }
    
    public static DoubleCheckedSingleton getInstance() {
        if (instance == null) {
            synchronized (DoubleCheckedSingleton.class) {
                if (instance == null) {
                    instance = new DoubleCheckedSingleton();
                }
            }
        }
        return instance;
    }
    
    public void showMessage() {
        System.out.println("Hello from Double-Checked Locking Singleton!");
    }
}
