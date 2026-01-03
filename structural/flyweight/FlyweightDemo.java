package structural.flyweight;

public class FlyweightDemo {
    private static final String[] colors = {"Red", "Green", "Blue", "Yellow", "Black"};
    
    public static void main(String[] args) {
        System.out.println("=== Flyweight Pattern Demo ===\n");
        
        System.out.println("Drawing 20 circles:\n");
        
        for (int i = 0; i < 20; i++) {
            String color = colors[(int) (Math.random() * colors.length)];
            Shape circle = ShapeFactory.getCircle(color);
            
            int x = (int) (Math.random() * 100);
            int y = (int) (Math.random() * 100);
            int radius = (int) (Math.random() * 10) + 1;
            
            circle.draw(x, y, radius);
        }
        
        System.out.println("\nTotal unique circle objects created: " + ShapeFactory.getTotalCircles());
    }
}
