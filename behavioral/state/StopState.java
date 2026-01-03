package behavioral.state;

public class StopState implements State {
    
    @Override
    public void handle(Context context) {
        System.out.println("Player is in STOP state");
        context.setState(this);
    }
    
    @Override
    public String toString() {
        return "Stop State";
    }
}
