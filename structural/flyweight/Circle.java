package structural.flyweight;

public class Circle implements Shape {
    private String color;
    
    public Circle(String color) {
        this.color = color;
        System.out.println("Creating circle of color: " + color);
    }
    
    @Override
    public void draw(int x, int y, int radius) {
        System.out.println("Drawing " + color + " circle at (" + x + ", " + y + ") with radius " + radius);
    }
}
