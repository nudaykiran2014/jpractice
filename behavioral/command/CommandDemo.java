package behavioral.command;

public class CommandDemo {
    public static void main(String[] args) {
        System.out.println("=== Command Pattern Demo ===\n");
        
        Light light = new Light();
        Fan fan = new Fan();
        
        Command lightOn = new LightOnCommand(light);
        Command lightOff = new LightOffCommand(light);
        Command fanOn = new FanOnCommand(fan);
        
        RemoteControl remote = new RemoteControl();
        
        System.out.println("Turning light on:");
        remote.setCommand(lightOn);
        remote.pressButton();
        
        System.out.println("\nTurning fan on:");
        remote.setCommand(fanOn);
        remote.pressButton();
        
        System.out.println("\nTurning light off:");
        remote.setCommand(lightOff);
        remote.pressButton();
        
        System.out.println("\nUndo last command:");
        remote.pressUndo();
        
        System.out.println("\nUndo previous command:");
        remote.pressUndo();
        
        System.out.println("\nUndo previous command:");
        remote.pressUndo();
    }
}
