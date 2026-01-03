package creational.abstractfactory;

public class MacCheckbox implements Checkbox {
    @Override
    public void render() {
        System.out.println("Rendering Mac style checkbox");
    }
    
    @Override
    public void toggle() {
        System.out.println("Mac checkbox toggled");
    }
}
