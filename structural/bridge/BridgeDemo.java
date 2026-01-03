package structural.bridge;

public class BridgeDemo {
    public static void main(String[] args) {
        System.out.println("=== Bridge Pattern Demo ===\n");
        
        System.out.println("Testing TV with basic remote:");
        Device tv = new TV();
        RemoteControl remote = new RemoteControl(tv);
        remote.togglePower();
        remote.volumeUp();
        remote.channelUp();
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        System.out.println("Testing Radio with advanced remote:");
        Device radio = new Radio();
        AdvancedRemoteControl advancedRemote = new AdvancedRemoteControl(radio);
        advancedRemote.togglePower();
        advancedRemote.volumeUp();
        advancedRemote.mute();
    }
}
