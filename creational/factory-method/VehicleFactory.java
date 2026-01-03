package creational.factorymethod;

public abstract class VehicleFactory {
    public abstract Vehicle createVehicle();
    
    public void deliverVehicle() {
        Vehicle vehicle = createVehicle();
        System.out.println("Delivering: " + vehicle.getType());
        vehicle.drive();
        vehicle.stop();
    }
}
