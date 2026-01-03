package creational.factorymethod;

public class Bike implements Vehicle {
    @Override
    public void drive() {
        System.out.println("Riding a bike on the road");
    }
    
    @Override
    public void stop() {
        System.out.println("Bike stopped");
    }
    
    @Override
    public String getType() {
        return "Bike";
    }
}
