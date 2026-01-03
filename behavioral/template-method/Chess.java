package behavioral.templatemethod;

public class Chess extends Game {
    
    @Override
    protected void initialize() {
        System.out.println("Chess Game Initialized! Start playing.");
    }
    
    @Override
    protected void startPlay() {
        System.out.println("Chess Game Started. Enjoy the game!");
    }
    
    @Override
    protected void endPlay() {
        System.out.println("Chess Game Finished!");
    }
}
