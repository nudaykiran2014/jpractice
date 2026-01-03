package structural.facade;

public class HomeTheaterFacade {
    private DVDPlayer dvdPlayer;
    private Projector projector;
    private SoundSystem soundSystem;
    private Lights lights;
    
    public HomeTheaterFacade(DVDPlayer dvdPlayer, Projector projector, 
                             SoundSystem soundSystem, Lights lights) {
        this.dvdPlayer = dvdPlayer;
        this.projector = projector;
        this.soundSystem = soundSystem;
        this.lights = lights;
    }
    
    public void watchMovie(String movie) {
        System.out.println("Get ready to watch a movie...\n");
        lights.dim(10);
        projector.on();
        projector.wideScreenMode();
        projector.setInput("DVD");
        soundSystem.on();
        soundSystem.setSurroundSound();
        soundSystem.setVolume(5);
        dvdPlayer.on();
        dvdPlayer.play(movie);
    }
    
    public void endMovie() {
        System.out.println("\nShutting down the home theater...\n");
        dvdPlayer.stop();
        dvdPlayer.off();
        soundSystem.off();
        projector.off();
        lights.on();
    }
}
