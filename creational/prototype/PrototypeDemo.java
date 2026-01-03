package creational.prototype;

public class PrototypeDemo {
    public static void main(String[] args) {
        System.out.println("=== Prototype Pattern Demo ===\n");
        
        ShapeCache.loadCache();
        
        System.out.println("Cloning shapes from cache:\n");
        
        Shape clonedCircle = ShapeCache.getShape("1");
        System.out.println("Shape: " + clonedCircle.getType());
        clonedCircle.draw();
        
        System.out.println();
        
        Shape clonedRectangle = ShapeCache.getShape("2");
        System.out.println("Shape: " + clonedRectangle.getType());
        clonedRectangle.draw();
        
        System.out.println();
        
        Shape clonedSquare = ShapeCache.getShape("3");
        System.out.println("Shape: " + clonedSquare.getType());
        clonedSquare.draw();
        
        System.out.println("\nModifying cloned shape:");
        clonedCircle.setColor("Yellow");
        clonedCircle.draw();
        
        System.out.println("\nOriginal shape remains unchanged:");
        Shape originalCircle = ShapeCache.getShape("1");
        originalCircle.draw();
    }
}
