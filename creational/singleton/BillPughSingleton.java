package creational.singleton;

public class BillPughSingleton {
    
    private BillPughSingleton() {
        System.out.println("BillPughSingleton instance created");
    }
    
    private static class SingletonHelper {
        private static final BillPughSingleton INSTANCE = new BillPughSingleton();
    }
    
    public static BillPughSingleton getInstance() {
        return SingletonHelper.INSTANCE;
    }
    
    public void showMessage() {
        System.out.println("Hello from Bill Pugh Singleton!");
    }
}
