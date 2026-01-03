package behavioral.state;

public class StateDemo {
    public static void main(String[] args) {
        System.out.println("=== State Pattern Demo ===\n");
        
        Context context = new Context();
        
        StartState startState = new StartState();
        startState.handle(context);
        System.out.println("Current state: " + context.getState().toString());
        
        System.out.println();
        
        PlayingState playingState = new PlayingState();
        playingState.handle(context);
        System.out.println("Current state: " + context.getState().toString());
        
        System.out.println();
        
        StopState stopState = new StopState();
        stopState.handle(context);
        System.out.println("Current state: " + context.getState().toString());
    }
}
