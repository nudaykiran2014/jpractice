package behavioral.command;

public class Fan {
    private boolean isOn = false;
    
    public void on() {
        isOn = true;
        System.out.println("Fan is ON");
    }
    
    public void off() {
        isOn = false;
        System.out.println("Fan is OFF");
    }
}
