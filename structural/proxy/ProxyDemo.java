package structural.proxy;

public class ProxyDemo {
    public static void main(String[] args) {
        System.out.println("=== Proxy Pattern Demo ===\n");
        
        System.out.println("Creating proxy images (no loading yet):");
        Image image1 = new ProxyImage("photo1.jpg");
        Image image2 = new ProxyImage("photo2.jpg");
        System.out.println("Proxy images created\n");
        
        System.out.println("First display call (loads from disk):");
        image1.display();
        
        System.out.println("\nSecond display call (uses cached image):");
        image1.display();
        
        System.out.println("\nDisplaying second image:");
        image2.display();
    }
}
