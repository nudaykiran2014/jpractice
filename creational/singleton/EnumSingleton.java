package creational.singleton;

public enum EnumSingleton {
    INSTANCE;
    
    public void showMessage() {
        System.out.println("Hello from Enum Singleton!");
    }
    
    public void doSomething() {
        System.out.println("Enum Singleton is doing something...");
    }
}
