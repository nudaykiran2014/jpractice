package creational.prototype;

public class Circle extends Shape {
    private int radius;
    
    public Circle() {
        type = "Circle";
    }
    
    public void setRadius(int radius) {
        this.radius = radius;
    }
    
    public int getRadius() {
        return radius;
    }
    
    @Override
    public void draw() {
        System.out.println("Drawing a " + color + " circle with radius " + radius);
    }
}
