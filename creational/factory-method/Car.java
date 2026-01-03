package creational.factorymethod;

public class Car implements Vehicle {
    @Override
    public void drive() {
        System.out.println("Driving a car on the road");
    }
    
    @Override
    public void stop() {
        System.out.println("Car stopped");
    }
    
    @Override
    public String getType() {
        return "Car";
    }
}
