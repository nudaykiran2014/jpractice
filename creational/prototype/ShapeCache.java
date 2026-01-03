package creational.prototype;

import java.util.HashMap;
import java.util.Map;

public class ShapeCache {
    private static Map<String, Shape> shapeMap = new HashMap<>();
    
    public static Shape getShape(String shapeId) {
        Shape cachedShape = shapeMap.get(shapeId);
        return cachedShape.clone();
    }
    
    public static void loadCache() {
        Circle circle = new Circle();
        circle.setId("1");
        circle.setColor("Red");
        circle.setRadius(5);
        shapeMap.put(circle.getId(), circle);
        
        Rectangle rectangle = new Rectangle();
        rectangle.setId("2");
        rectangle.setColor("Blue");
        rectangle.setWidth(10);
        rectangle.setHeight(5);
        shapeMap.put(rectangle.getId(), rectangle);
        
        Square square = new Square();
        square.setId("3");
        square.setColor("Green");
        square.setSide(7);
        shapeMap.put(square.getId(), square);
    }
}
