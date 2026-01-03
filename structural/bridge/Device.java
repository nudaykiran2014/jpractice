package structural.bridge;

public interface Device {
    void turnOn();
    void turnOff();
    void setVolume(int volume);
    int getVolume();
    void setChannel(int channel);
    int getChannel();
}
