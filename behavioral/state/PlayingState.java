package behavioral.state;

public class PlayingState implements State {
    
    @Override
    public void handle(Context context) {
        System.out.println("Player is in PLAYING state");
        context.setState(this);
    }
    
    @Override
    public String toString() {
        return "Playing State";
    }
}
