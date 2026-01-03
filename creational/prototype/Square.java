package creational.prototype;

public class Square extends Shape {
    private int side;
    
    public Square() {
        type = "Square";
    }
    
    public void setSide(int side) {
        this.side = side;
    }
    
    public int getSide() {
        return side;
    }
    
    @Override
    public void draw() {
        System.out.println("Drawing a " + color + " square with side " + side);
    }
}
