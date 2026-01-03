package creational.singleton;

public class SingletonDemo {
    public static void main(String[] args) {
        System.out.println("=== Singleton Pattern Demo ===\n");
        
        System.out.println("1. Eager Singleton:");
        EagerSingleton eager1 = EagerSingleton.getInstance();
        EagerSingleton eager2 = EagerSingleton.getInstance();
        System.out.println("Same instance? " + (eager1 == eager2));
        eager1.showMessage();
        
        System.out.println("\n2. Lazy Singleton:");
        LazySingleton lazy1 = LazySingleton.getInstance();
        LazySingleton lazy2 = LazySingleton.getInstance();
        System.out.println("Same instance? " + (lazy1 == lazy2));
        lazy1.showMessage();
        
        System.out.println("\n3. Thread-Safe Singleton:");
        ThreadSafeSingleton threadSafe1 = ThreadSafeSingleton.getInstance();
        ThreadSafeSingleton threadSafe2 = ThreadSafeSingleton.getInstance();
        System.out.println("Same instance? " + (threadSafe1 == threadSafe2));
        threadSafe1.showMessage();
        
        System.out.println("\n4. Double-Checked Locking Singleton:");
        DoubleCheckedSingleton doubleChecked1 = DoubleCheckedSingleton.getInstance();
        DoubleCheckedSingleton doubleChecked2 = DoubleCheckedSingleton.getInstance();
        System.out.println("Same instance? " + (doubleChecked1 == doubleChecked2));
        doubleChecked1.showMessage();
        
        System.out.println("\n5. Bill Pugh Singleton:");
        BillPughSingleton billPugh1 = BillPughSingleton.getInstance();
        BillPughSingleton billPugh2 = BillPughSingleton.getInstance();
        System.out.println("Same instance? " + (billPugh1 == billPugh2));
        billPugh1.showMessage();
        
        System.out.println("\n6. Enum Singleton:");
        EnumSingleton enumSingleton1 = EnumSingleton.INSTANCE;
        EnumSingleton enumSingleton2 = EnumSingleton.INSTANCE;
        System.out.println("Same instance? " + (enumSingleton1 == enumSingleton2));
        enumSingleton1.showMessage();
        enumSingleton1.doSomething();
    }
}
