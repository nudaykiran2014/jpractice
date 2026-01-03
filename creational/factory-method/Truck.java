package creational.factorymethod;

public class Truck implements Vehicle {
    @Override
    public void drive() {
        System.out.println("Driving a truck on the highway");
    }
    
    @Override
    public void stop() {
        System.out.println("Truck stopped");
    }
    
    @Override
    public String getType() {
        return "Truck";
    }
}
