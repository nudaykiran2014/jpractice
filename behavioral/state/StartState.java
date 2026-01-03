package behavioral.state;

public class StartState implements State {
    
    @Override
    public void handle(Context context) {
        System.out.println("Player is in START state");
        context.setState(this);
    }
    
    @Override
    public String toString() {
        return "Start State";
    }
}
