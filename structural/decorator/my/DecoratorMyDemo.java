/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * DECORATOR PATTERN - Explained Like You're a Kid! 🧒
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * STORY TIME! 📖
 * ──────────────
 * Imagine you're wrapping a BIRTHDAY GIFT 🎁
 * 
 * You start with a plain box:
 *   📦 Box
 *   
 * Then you WRAP it with colored paper:
 *   📦 ──→ 🎨 Colored Paper
 *   
 * Then you add a RIBBON:
 *   📦 ──→ 🎨 ──→ 🎀 Ribbon
 *   
 * Then you add a CARD:
 *   📦 ──→ 🎨 ──→ 🎀 ──→ 💌 Card
 * 
 * Each layer WRAPS the previous one and ADDS something!
 * The box is still a box, just with extra decorations!
 * 
 * THE PATTERN:
 * ─────────────
 *     Component (interface)
 *          │
 *     ┌────┴────┐
 *     │         │
 *  Concrete   Decorator ──────┐
 *  Component    │             │
 *           ┌───┴───┐         │
 *        Decorator1  Decorator2  (wraps Component)
 *        
 *     Each decorator HAS-A component and IS-A component!
 */

// ═══════════════════════════════════════════════════════════════════════════════
// COMPONENT - Base interface
// ═══════════════════════════════════════════════════════════════════════════════
interface Pizza {
    String getDescription();
    double getCost();
}

// ═══════════════════════════════════════════════════════════════════════════════
// CONCRETE COMPONENT - Basic pizzas
// ═══════════════════════════════════════════════════════════════════════════════
class MargheritaPizza implements Pizza {
    public String getDescription() { return "Margherita Pizza"; }
    public double getCost() { return 200; }
}

class FarmhousePizza implements Pizza {
    public String getDescription() { return "Farmhouse Pizza"; }
    public double getCost() { return 250; }
}

// ═══════════════════════════════════════════════════════════════════════════════
// DECORATOR - Base decorator (wraps pizza)
// ═══════════════════════════════════════════════════════════════════════════════
abstract class ToppingDecorator implements Pizza {
    protected Pizza pizza;  // The wrapped pizza
    
    public ToppingDecorator(Pizza pizza) {
        this.pizza = pizza;
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// CONCRETE DECORATORS - Toppings
// ═══════════════════════════════════════════════════════════════════════════════
class CheeseDecorator extends ToppingDecorator {
    public CheeseDecorator(Pizza pizza) {
        super(pizza);
    }
    
    public String getDescription() {
        return pizza.getDescription() + " + Extra Cheese 🧀";
    }
    
    public double getCost() {
        return pizza.getCost() + 50;
    }
}

class MushroomDecorator extends ToppingDecorator {
    public MushroomDecorator(Pizza pizza) {
        super(pizza);
    }
    
    public String getDescription() {
        return pizza.getDescription() + " + Mushroom 🍄";
    }
    
    public double getCost() {
        return pizza.getCost() + 40;
    }
}

class JalapenoDecorator extends ToppingDecorator {
    public JalapenoDecorator(Pizza pizza) {
        super(pizza);
    }
    
    public String getDescription() {
        return pizza.getDescription() + " + Jalapeno 🌶️";
    }
    
    public double getCost() {
        return pizza.getCost() + 30;
    }
}

class OliveDecorator extends ToppingDecorator {
    public OliveDecorator(Pizza pizza) {
        super(pizza);
    }
    
    public String getDescription() {
        return pizza.getDescription() + " + Olives 🫒";
    }
    
    public double getCost() {
        return pizza.getCost() + 35;
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// REAL-WORLD EXAMPLE: Text Formatting
// ═══════════════════════════════════════════════════════════════════════════════

interface Text {
    String render();
}

class PlainText implements Text {
    private String content;
    
    public PlainText(String content) {
        this.content = content;
    }
    
    public String render() {
        return content;
    }
}

abstract class TextDecorator implements Text {
    protected Text text;
    
    public TextDecorator(Text text) {
        this.text = text;
    }
}

class BoldDecorator extends TextDecorator {
    public BoldDecorator(Text text) { super(text); }
    
    public String render() {
        return "<b>" + text.render() + "</b>";
    }
}

class ItalicDecorator extends TextDecorator {
    public ItalicDecorator(Text text) { super(text); }
    
    public String render() {
        return "<i>" + text.render() + "</i>";
    }
}

class UnderlineDecorator extends TextDecorator {
    public UnderlineDecorator(Text text) { super(text); }
    
    public String render() {
        return "<u>" + text.render() + "</u>";
    }
}

class ColorDecorator extends TextDecorator {
    private String color;
    
    public ColorDecorator(Text text, String color) {
        super(text);
        this.color = color;
    }
    
    public String render() {
        return "<span style='color:" + color + "'>" + text.render() + "</span>";
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// DEMO
// ═══════════════════════════════════════════════════════════════════════════════
public class DecoratorMyDemo {
    
    public static void main(String[] args) {
        System.out.println("═══ DECORATOR PATTERN - PIZZA ORDER ═══\n");
        
        // Basic pizza
        Pizza pizza1 = new MargheritaPizza();
        System.out.println("Order 1: " + pizza1.getDescription());
        System.out.println("  Cost: ₹" + pizza1.getCost());
        
        // Pizza with cheese
        Pizza pizza2 = new CheeseDecorator(new MargheritaPizza());
        System.out.println("\nOrder 2: " + pizza2.getDescription());
        System.out.println("  Cost: ₹" + pizza2.getCost());
        
        // Pizza with multiple toppings
        Pizza pizza3 = new MargheritaPizza();
        pizza3 = new CheeseDecorator(pizza3);
        pizza3 = new MushroomDecorator(pizza3);
        pizza3 = new JalapenoDecorator(pizza3);
        System.out.println("\nOrder 3: " + pizza3.getDescription());
        System.out.println("  Cost: ₹" + pizza3.getCost());
        
        // Farmhouse with everything!
        Pizza pizza4 = new OliveDecorator(
                         new JalapenoDecorator(
                           new MushroomDecorator(
                             new CheeseDecorator(
                               new FarmhousePizza()))));
        System.out.println("\nOrder 4: " + pizza4.getDescription());
        System.out.println("  Cost: ₹" + pizza4.getCost());
        
        System.out.println("\n\n═══ DECORATOR PATTERN - TEXT FORMATTING ═══\n");
        
        // Plain text
        Text text1 = new PlainText("Hello World");
        System.out.println("Plain: " + text1.render());
        
        // Bold text
        Text text2 = new BoldDecorator(new PlainText("Hello World"));
        System.out.println("Bold: " + text2.render());
        
        // Bold + Italic
        Text text3 = new ItalicDecorator(
                       new BoldDecorator(
                         new PlainText("Hello World")));
        System.out.println("Bold+Italic: " + text3.render());
        
        // All decorations!
        Text text4 = new ColorDecorator(
                       new UnderlineDecorator(
                         new ItalicDecorator(
                           new BoldDecorator(
                             new PlainText("Hello World")))), "red");
        System.out.println("All styles: " + text4.render());
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * WITHOUT DECORATOR (BAD - Class Explosion):
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     class MargheritaPizza { }
 *     class MargheritaWithCheese { }
 *     class MargheritaWithMushroom { }
 *     class MargheritaWithCheeseAndMushroom { }
 *     class MargheritaWithCheeseAndJalapeno { }
 *     // ... 100s of classes for each combination!
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * WITH DECORATOR (GOOD):
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     Pizza pizza = new MargheritaPizza();
 *     pizza = new CheeseDecorator(pizza);
 *     pizza = new MushroomDecorator(pizza);
 *     // Add any combination dynamically!
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * REAL-WORLD EXAMPLES:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     1. JAVA I/O STREAMS
 *        - BufferedInputStream(FileInputStream(file))
 *        - Each wraps and adds buffering!
 *     
 *     2. STARBUCKS COFFEE
 *        - Coffee + Milk + Syrup + Whipped Cream
 *     
 *     3. SPRING SECURITY
 *        - FilterChain wraps request handlers
 *     
 *     4. GUI SCROLL BARS
 *        - ScrollDecorator wraps text area
 *     
 *     5. HTTP MIDDLEWARE
 *        - Logging → Auth → Compression → Handler
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * DECORATOR vs INHERITANCE:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     INHERITANCE (static):
 *        - Decided at compile time
 *        - Can't change at runtime
 *        - Class explosion for combinations
 *     
 *     DECORATOR (dynamic):
 *        - Decided at runtime
 *        - Can add/remove features dynamically
 *        - Flexible combinations
 */
