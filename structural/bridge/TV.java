package structural.bridge;

public class TV implements Device {
    private boolean on = false;
    private int volume = 30;
    private int channel = 1;
    
    @Override
    public void turnOn() {
        on = true;
        System.out.println("TV is now ON");
    }
    
    @Override
    public void turnOff() {
        on = false;
        System.out.println("TV is now OFF");
    }
    
    @Override
    public void setVolume(int volume) {
        this.volume = volume;
        System.out.println("TV volume set to " + volume);
    }
    
    @Override
    public int getVolume() {
        return volume;
    }
    
    @Override
    public void setChannel(int channel) {
        this.channel = channel;
        System.out.println("TV channel set to " + channel);
    }
    
    @Override
    public int getChannel() {
        return channel;
    }
}
