package creational.builder;

public class Computer {
    private String CPU;
    private String RAM;
    private String storage;
    private String GPU;
    private String motherboard;
    private String powerSupply;
    private boolean hasWiFi;
    private boolean hasBluetooth;
    
    private Computer(Builder builder) {
        this.CPU = builder.CPU;
        this.RAM = builder.RAM;
        this.storage = builder.storage;
        this.GPU = builder.GPU;
        this.motherboard = builder.motherboard;
        this.powerSupply = builder.powerSupply;
        this.hasWiFi = builder.hasWiFi;
        this.hasBluetooth = builder.hasBluetooth;
    }
    
    public static class Builder {
        private String CPU;
        private String RAM;
        private String storage;
        private String GPU = "Integrated";
        private String motherboard;
        private String powerSupply;
        private boolean hasWiFi = false;
        private boolean hasBluetooth = false;
        
        public Builder(String CPU, String RAM, String storage) {
            this.CPU = CPU;
            this.RAM = RAM;
            this.storage = storage;
        }
        
        public Builder setGPU(String GPU) {
            this.GPU = GPU;
            return this;
        }
        
        public Builder setMotherboard(String motherboard) {
            this.motherboard = motherboard;
            return this;
        }
        
        public Builder setPowerSupply(String powerSupply) {
            this.powerSupply = powerSupply;
            return this;
        }
        
        public Builder setWiFi(boolean hasWiFi) {
            this.hasWiFi = hasWiFi;
            return this;
        }
        
        public Builder setBluetooth(boolean hasBluetooth) {
            this.hasBluetooth = hasBluetooth;
            return this;
        }
        
        public Computer build() {
            return new Computer(this);
        }
    }
    
    @Override
    public String toString() {
        return "Computer Configuration:\n" +
               "  CPU: " + CPU + "\n" +
               "  RAM: " + RAM + "\n" +
               "  Storage: " + storage + "\n" +
               "  GPU: " + GPU + "\n" +
               "  Motherboard: " + motherboard + "\n" +
               "  Power Supply: " + powerSupply + "\n" +
               "  WiFi: " + (hasWiFi ? "Yes" : "No") + "\n" +
               "  Bluetooth: " + (hasBluetooth ? "Yes" : "No");
    }
}
