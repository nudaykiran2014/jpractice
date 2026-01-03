package structural.bridge;

public class Radio implements Device {
    private boolean on = false;
    private int volume = 20;
    private int channel = 1;
    
    @Override
    public void turnOn() {
        on = true;
        System.out.println("Radio is now ON");
    }
    
    @Override
    public void turnOff() {
        on = false;
        System.out.println("Radio is now OFF");
    }
    
    @Override
    public void setVolume(int volume) {
        this.volume = volume;
        System.out.println("Radio volume set to " + volume);
    }
    
    @Override
    public int getVolume() {
        return volume;
    }
    
    @Override
    public void setChannel(int channel) {
        this.channel = channel;
        System.out.println("Radio station set to " + channel);
    }
    
    @Override
    public int getChannel() {
        return channel;
    }
}
