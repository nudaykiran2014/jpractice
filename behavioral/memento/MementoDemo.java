package behavioral.memento;

public class MementoDemo {
    public static void main(String[] args) {
        System.out.println("=== Memento Pattern Demo ===\n");
        
        TextEditor editor = new TextEditor();
        History history = new History();
        
        editor.write("Version 1");
        System.out.println("Current content: " + editor.getContent());
        history.push(editor.save());
        
        editor.write("Version 2");
        System.out.println("Current content: " + editor.getContent());
        history.push(editor.save());
        
        editor.write("Version 3");
        System.out.println("Current content: " + editor.getContent());
        
        System.out.println("\nRestoring to previous version:");
        editor.restore(history.pop());
        System.out.println("Current content: " + editor.getContent());
        
        System.out.println("\nRestoring to previous version:");
        editor.restore(history.pop());
        System.out.println("Current content: " + editor.getContent());
    }
}
