package creational.abstractfactory;

public class AbstractFactoryDemo {
    public static void main(String[] args) {
        System.out.println("=== Abstract Factory Pattern Demo ===\n");
        
        System.out.println("Creating Windows Application:");
        GUIFactory windowsFactory = new WindowsFactory();
        Application windowsApp = new Application(windowsFactory);
        windowsApp.render();
        windowsApp.interact();
        
        System.out.println("\nCreating Mac Application:");
        GUIFactory macFactory = new MacFactory();
        Application macApp = new Application(macFactory);
        macApp.render();
        macApp.interact();
    }
}
