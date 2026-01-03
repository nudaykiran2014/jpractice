package creational.factorymethod;

public class FactoryMethodDemo {
    public static void main(String[] args) {
        System.out.println("=== Factory Method Pattern Demo ===\n");
        
        VehicleFactory carFactory = new CarFactory();
        carFactory.deliverVehicle();
        
        System.out.println();
        
        VehicleFactory bikeFactory = new BikeFactory();
        bikeFactory.deliverVehicle();
        
        System.out.println();
        
        VehicleFactory truckFactory = new TruckFactory();
        truckFactory.deliverVehicle();
    }
}
