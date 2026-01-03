package structural.facade;

public class FacadeDemo {
    public static void main(String[] args) {
        System.out.println("=== Facade Pattern Demo ===\n");
        
        DVDPlayer dvdPlayer = new DVDPlayer();
        Projector projector = new Projector();
        SoundSystem soundSystem = new SoundSystem();
        Lights lights = new Lights();
        
        HomeTheaterFacade homeTheater = new HomeTheaterFacade(
            dvdPlayer, projector, soundSystem, lights
        );
        
        homeTheater.watchMovie("The Matrix");
        
        homeTheater.endMovie();
    }
}
