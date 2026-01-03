package behavioral.command;

public class Light {
    private boolean isOn = false;
    
    public void on() {
        isOn = true;
        System.out.println("Light is ON");
    }
    
    public void off() {
        isOn = false;
        System.out.println("Light is OFF");
    }
}
