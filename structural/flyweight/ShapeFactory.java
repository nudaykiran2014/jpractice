package structural.flyweight;

import java.util.HashMap;
import java.util.Map;

public class ShapeFactory {
    private static final Map<String, Shape> circleMap = new HashMap<>();
    
    public static Shape getCircle(String color) {
        Circle circle = (Circle) circleMap.get(color);
        
        if (circle == null) {
            circle = new Circle(color);
            circleMap.put(color, circle);
            System.out.println("Created new circle flyweight for color: " + color);
        } else {
            System.out.println("Reusing existing circle flyweight for color: " + color);
        }
        
        return circle;
    }
    
    public static int getTotalCircles() {
        return circleMap.size();
    }
}
