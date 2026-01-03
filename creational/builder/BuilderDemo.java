package creational.builder;

public class BuilderDemo {
    public static void main(String[] args) {
        System.out.println("=== Builder Pattern Demo ===\n");
        
        System.out.println("Building a Gaming Computer:");
        Computer gamingPC = new Computer.Builder("Intel i9", "32GB", "1TB NVMe SSD")
                .setGPU("NVIDIA RTX 4090")
                .setMotherboard("ASUS ROG")
                .setPowerSupply("850W")
                .setWiFi(true)
                .setBluetooth(true)
                .build();
        System.out.println(gamingPC);
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        System.out.println("Building an Office Computer:");
        Computer officePC = new Computer.Builder("Intel i5", "16GB", "512GB SSD")
                .setMotherboard("Standard ATX")
                .setPowerSupply("500W")
                .setWiFi(true)
                .build();
        System.out.println(officePC);
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        System.out.println("Building a Budget Computer:");
        Computer budgetPC = new Computer.Builder("AMD Ryzen 3", "8GB", "256GB SSD")
                .setPowerSupply("400W")
                .build();
        System.out.println(budgetPC);
    }
}
