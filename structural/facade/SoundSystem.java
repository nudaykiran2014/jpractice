package structural.facade;

public class SoundSystem {
    public void on() {
        System.out.println("Sound System is ON");
    }
    
    public void setVolume(int level) {
        System.out.println("Sound System volume set to: " + level);
    }
    
    public void setSurroundSound() {
        System.out.println("Sound System set to surround sound");
    }
    
    public void off() {
        System.out.println("Sound System is OFF");
    }
}
