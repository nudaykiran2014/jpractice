package behavioral.memento;

public class TextEditor {
    private String content;
    
    public void write(String text) {
        this.content = text;
    }
    
    public String getContent() {
        return content;
    }
    
    public Memento save() {
        return new Memento(content);
    }
    
    public void restore(Memento memento) {
        this.content = memento.getState();
    }
}
