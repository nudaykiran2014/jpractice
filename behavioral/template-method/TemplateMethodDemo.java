package behavioral.templatemethod;

public class TemplateMethodDemo {
    public static void main(String[] args) {
        System.out.println("=== Template Method Pattern Demo ===\n");
        
        Game game = new Cricket();
        game.play();
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        game = new Football();
        game.play();
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        game = new Chess();
        game.play();
    }
}
