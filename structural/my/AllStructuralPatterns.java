/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * ALL 7 STRUCTURAL PATTERNS - Explained Like You're a Kid! 🧒
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * STRUCTURAL PATTERNS = How to COMBINE objects/classes together
 * 
 * Think of them like LEGO blocks 🧱 - different ways to connect pieces!
 * 
 * THE 7 PATTERNS:
 * ───────────────
 *     1. ADAPTER     - 🔌 "Power plug converter"
 *     2. BRIDGE      - 🌉 "Separate what from how"
 *     3. COMPOSITE   - 🌳 "Tree structure"
 *     4. DECORATOR   - 🎁 "Gift wrapping"
 *     5. FACADE      - 🏠 "Simple front door"
 *     6. FLYWEIGHT   - 🪶 "Share to save memory"
 *     7. PROXY       - 🛡️ "Bodyguard/Representative"
 */

import java.util.*;

// ═══════════════════════════════════════════════════════════════════════════════
// 1. ADAPTER - "Power Plug Converter" 🔌
// ═══════════════════════════════════════════════════════════════════════════════
/*
 * PROBLEM: You have a US plug 🔌 but Indian socket 🔲
 * SOLUTION: Use an ADAPTER to make them work together!
 * 
 *     US Plug ──→ [ADAPTER] ──→ Indian Socket
 *     
 * USE WHEN: You want to use an existing class but its interface doesn't match
 */

// Old interface (what we have)
interface OldPrinter {
    void printOld(String text);
}

class LegacyPrinter implements OldPrinter {
    public void printOld(String text) {
        System.out.println("    [Legacy] " + text);
    }
}

// New interface (what we need)
interface ModernPrinter {
    void print(String text);
}

// ADAPTER - makes old work with new!
class PrinterAdapter implements ModernPrinter {
    private OldPrinter oldPrinter;
    
    public PrinterAdapter(OldPrinter oldPrinter) {
        this.oldPrinter = oldPrinter;
    }
    
    public void print(String text) {
        oldPrinter.printOld(text);  // Delegates to old!
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// 2. BRIDGE - "Separate What from How" 🌉
// ═══════════════════════════════════════════════════════════════════════════════
/*
 * PROBLEM: Shape + Color = Explosion of classes!
 *          RedCircle, BlueCircle, RedSquare, BlueSquare...
 * 
 * SOLUTION: Separate SHAPE (what) from COLOR (how)
 * 
 *     Shape ────🌉──── Color
 *       │               │
 *    Circle          Red
 *    Square          Blue
 *    
 * USE WHEN: You have multiple dimensions of variation
 */

// Implementor (the "how")
interface DrawingAPI {
    void drawCircle(int x, int y, int radius);
}

class RedDrawing implements DrawingAPI {
    public void drawCircle(int x, int y, int radius) {
        System.out.println("    🔴 Red circle at (" + x + "," + y + ") r=" + radius);
    }
}

class BlueDrawing implements DrawingAPI {
    public void drawCircle(int x, int y, int radius) {
        System.out.println("    🔵 Blue circle at (" + x + "," + y + ") r=" + radius);
    }
}

// Abstraction (the "what") - BRIDGES to implementor
abstract class Shape {
    protected DrawingAPI drawingAPI;  // 🌉 The bridge!
    
    public Shape(DrawingAPI api) {
        this.drawingAPI = api;
    }
    
    abstract void draw();
}

class CircleShape extends Shape {
    private int x, y, radius;
    
    public CircleShape(int x, int y, int radius, DrawingAPI api) {
        super(api);
        this.x = x; this.y = y; this.radius = radius;
    }
    
    void draw() {
        drawingAPI.drawCircle(x, y, radius);
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// 3. COMPOSITE - "Tree Structure" 🌳
// ═══════════════════════════════════════════════════════════════════════════════
/*
 * PROBLEM: Folder contains files AND other folders
 *          How to treat them uniformly?
 * 
 * SOLUTION: Both File and Folder implement same interface
 * 
 *     📁 Folder
 *      ├── 📄 File1
 *      ├── 📄 File2
 *      └── 📁 SubFolder
 *           ├── 📄 File3
 *           └── 📄 File4
 *           
 * USE WHEN: You have tree structures (part-whole hierarchies)
 */

interface FileSystemItem {
    void showDetails(String indent);
    int getSize();
}

class File implements FileSystemItem {
    private String name;
    private int size;
    
    public File(String name, int size) {
        this.name = name;
        this.size = size;
    }
    
    public void showDetails(String indent) {
        System.out.println(indent + "📄 " + name + " (" + size + "KB)");
    }
    
    public int getSize() { return size; }
}

class Folder implements FileSystemItem {
    private String name;
    private List<FileSystemItem> items = new ArrayList<>();
    
    public Folder(String name) {
        this.name = name;
    }
    
    public void add(FileSystemItem item) {
        items.add(item);
    }
    
    public void showDetails(String indent) {
        System.out.println(indent + "📁 " + name + " (" + getSize() + "KB)");
        for (FileSystemItem item : items) {
            item.showDetails(indent + "  ");
        }
    }
    
    public int getSize() {
        return items.stream().mapToInt(FileSystemItem::getSize).sum();
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// 4. DECORATOR - "Gift Wrapping" 🎁
// ═══════════════════════════════════════════════════════════════════════════════
/*
 * PROBLEM: Add features to object without changing its class
 *          Coffee + Milk + Sugar + Cream...
 * 
 * SOLUTION: Wrap object with decorators
 * 
 *     ☕ Coffee
 *       └── 🥛 MilkDecorator
 *             └── 🍬 SugarDecorator
 *             
 * USE WHEN: Add responsibilities dynamically, alternative to subclassing
 */

interface Coffee {
    String getDescription();
    double getCost();
}

class SimpleCoffee implements Coffee {
    public String getDescription() { return "Coffee"; }
    public double getCost() { return 50.0; }
}

// Base Decorator
abstract class CoffeeDecorator implements Coffee {
    protected Coffee coffee;
    
    public CoffeeDecorator(Coffee coffee) {
        this.coffee = coffee;
    }
}

class MilkDecorator extends CoffeeDecorator {
    public MilkDecorator(Coffee coffee) { super(coffee); }
    
    public String getDescription() {
        return coffee.getDescription() + " + Milk";
    }
    
    public double getCost() {
        return coffee.getCost() + 10.0;
    }
}

class SugarDecorator extends CoffeeDecorator {
    public SugarDecorator(Coffee coffee) { super(coffee); }
    
    public String getDescription() {
        return coffee.getDescription() + " + Sugar";
    }
    
    public double getCost() {
        return coffee.getCost() + 5.0;
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// 5. FACADE - "Simple Front Door" 🏠
// ═══════════════════════════════════════════════════════════════════════════════
/*
 * PROBLEM: Complex subsystem with many classes
 *          Client needs to know too much!
 * 
 * SOLUTION: Simple interface that hides complexity
 * 
 *     Client ──→ 🏠 Facade ──→ Complex Subsystem
 *                              (CPU, Memory, HardDrive)
 *                              
 * USE WHEN: Simplify access to complex system
 */

// Complex subsystem classes
class CPU {
    void start() { System.out.println("    CPU starting..."); }
}

class Memory {
    void load() { System.out.println("    Memory loading..."); }
}

class HardDrive {
    void read() { System.out.println("    HardDrive reading..."); }
}

// FACADE - simple interface
class ComputerFacade {
    private CPU cpu = new CPU();
    private Memory memory = new Memory();
    private HardDrive hardDrive = new HardDrive();
    
    // One simple method hides all complexity!
    public void startComputer() {
        System.out.println("    🖥️ Starting computer...");
        cpu.start();
        memory.load();
        hardDrive.read();
        System.out.println("    ✅ Computer ready!");
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// 6. FLYWEIGHT - "Share to Save Memory" 🪶
// ═══════════════════════════════════════════════════════════════════════════════
/*
 * PROBLEM: Creating millions of similar objects = Out of Memory!
 *          Game with 10,000 trees, each with same texture
 * 
 * SOLUTION: Share common data, store unique data separately
 * 
 *     Tree1 ──┐
 *     Tree2 ──┼──→ 🌲 Shared TreeType (texture, mesh)
 *     Tree3 ──┘
 *     
 * USE WHEN: Many objects with shared immutable state
 */

// Flyweight (shared data)
class TreeType {
    private String name;
    private String texture;
    
    public TreeType(String name, String texture) {
        this.name = name;
        this.texture = texture;
        System.out.println("    🌲 Created TreeType: " + name + " (expensive!)");
    }
    
    public void draw(int x, int y) {
        System.out.println("    Drawing " + name + " at (" + x + "," + y + ")");
    }
}

// Flyweight Factory
class TreeFactory {
    private static Map<String, TreeType> treeTypes = new HashMap<>();
    
    public static TreeType getTreeType(String name, String texture) {
        if (!treeTypes.containsKey(name)) {
            treeTypes.put(name, new TreeType(name, texture));
        }
        return treeTypes.get(name);
    }
    
    public static int getTotalTypes() { return treeTypes.size(); }
}

// Context (unique data)
class Tree {
    private int x, y;
    private TreeType type;  // Shared!
    
    public Tree(int x, int y, TreeType type) {
        this.x = x; this.y = y; this.type = type;
    }
    
    public void draw() { type.draw(x, y); }
}

// ═══════════════════════════════════════════════════════════════════════════════
// 7. PROXY - "Bodyguard/Representative" 🛡️
// ═══════════════════════════════════════════════════════════════════════════════
/*
 * PROBLEM: Need to control access to an object
 *          - Lazy loading (expensive to create)
 *          - Access control (check permissions)
 *          - Logging (track usage)
 * 
 * SOLUTION: Proxy stands in front of real object
 * 
 *     Client ──→ 🛡️ Proxy ──→ Real Object
 *     
 * USE WHEN: Control access, lazy loading, caching, logging
 */

interface Image {
    void display();
}

// Real object (expensive to create)
class RealImage implements Image {
    private String filename;
    
    public RealImage(String filename) {
        this.filename = filename;
        loadFromDisk();  // Expensive!
    }
    
    private void loadFromDisk() {
        System.out.println("    ⏳ Loading image: " + filename + " (slow!)");
        try { Thread.sleep(100); } catch (Exception e) {}
    }
    
    public void display() {
        System.out.println("    🖼️ Displaying: " + filename);
    }
}

// Proxy (controls access)
class ImageProxy implements Image {
    private String filename;
    private RealImage realImage;  // Created only when needed!
    
    public ImageProxy(String filename) {
        this.filename = filename;
        System.out.println("    ⚡ Created proxy for: " + filename + " (fast!)");
    }
    
    public void display() {
        if (realImage == null) {
            realImage = new RealImage(filename);  // Lazy loading!
        }
        realImage.display();
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// DEMO
// ═══════════════════════════════════════════════════════════════════════════════
public class AllStructuralPatterns {
    
    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("           ALL 7 STRUCTURAL PATTERNS DEMO");
        System.out.println("═══════════════════════════════════════════════════════════════\n");
        
        // 1. ADAPTER
        System.out.println("1️⃣ ADAPTER - Power Plug Converter 🔌");
        System.out.println("─────────────────────────────────────");
        OldPrinter oldPrinter = new LegacyPrinter();
        ModernPrinter adapter = new PrinterAdapter(oldPrinter);
        adapter.print("Hello via adapter!");
        System.out.println();
        
        // 2. BRIDGE
        System.out.println("2️⃣ BRIDGE - Separate What from How 🌉");
        System.out.println("─────────────────────────────────────");
        Shape redCircle = new CircleShape(10, 10, 5, new RedDrawing());
        Shape blueCircle = new CircleShape(20, 20, 10, new BlueDrawing());
        redCircle.draw();
        blueCircle.draw();
        System.out.println();
        
        // 3. COMPOSITE
        System.out.println("3️⃣ COMPOSITE - Tree Structure 🌳");
        System.out.println("─────────────────────────────────────");
        Folder root = new Folder("Root");
        root.add(new File("file1.txt", 10));
        root.add(new File("file2.txt", 20));
        Folder subFolder = new Folder("SubFolder");
        subFolder.add(new File("file3.txt", 30));
        root.add(subFolder);
        root.showDetails("    ");
        System.out.println();
        
        // 4. DECORATOR
        System.out.println("4️⃣ DECORATOR - Gift Wrapping 🎁");
        System.out.println("─────────────────────────────────────");
        Coffee coffee = new SimpleCoffee();
        System.out.println("    " + coffee.getDescription() + " = ₹" + coffee.getCost());
        coffee = new MilkDecorator(coffee);
        System.out.println("    " + coffee.getDescription() + " = ₹" + coffee.getCost());
        coffee = new SugarDecorator(coffee);
        System.out.println("    " + coffee.getDescription() + " = ₹" + coffee.getCost());
        System.out.println();
        
        // 5. FACADE
        System.out.println("5️⃣ FACADE - Simple Front Door 🏠");
        System.out.println("─────────────────────────────────────");
        ComputerFacade computer = new ComputerFacade();
        computer.startComputer();
        System.out.println();
        
        // 6. FLYWEIGHT
        System.out.println("6️⃣ FLYWEIGHT - Share to Save Memory 🪶");
        System.out.println("─────────────────────────────────────");
        System.out.println("    Creating 5 trees (but only 2 types):");
        Tree[] trees = new Tree[5];
        trees[0] = new Tree(1, 1, TreeFactory.getTreeType("Oak", "oak.png"));
        trees[1] = new Tree(2, 2, TreeFactory.getTreeType("Pine", "pine.png"));
        trees[2] = new Tree(3, 3, TreeFactory.getTreeType("Oak", "oak.png"));  // Reuses!
        trees[3] = new Tree(4, 4, TreeFactory.getTreeType("Oak", "oak.png"));  // Reuses!
        trees[4] = new Tree(5, 5, TreeFactory.getTreeType("Pine", "pine.png")); // Reuses!
        System.out.println("    Total TreeType objects created: " + TreeFactory.getTotalTypes());
        System.out.println();
        
        // 7. PROXY
        System.out.println("7️⃣ PROXY - Bodyguard 🛡️");
        System.out.println("─────────────────────────────────────");
        System.out.println("    Creating proxies (fast, no loading):");
        Image img1 = new ImageProxy("photo1.jpg");
        Image img2 = new ImageProxy("photo2.jpg");
        System.out.println("\n    Now displaying (lazy loads):");
        img1.display();
        System.out.println("\n    Displaying again (already loaded):");
        img1.display();
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * QUICK REFERENCE CHEAT SHEET:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *  Pattern      │ Analogy              │ Use When
 * ─────────────┼──────────────────────┼─────────────────────────────────────
 *  ADAPTER     │ 🔌 Power converter   │ Make incompatible interfaces work
 *  BRIDGE      │ 🌉 Separate concerns │ Multiple dimensions of variation
 *  COMPOSITE   │ 🌳 Folder structure  │ Tree/part-whole hierarchies
 *  DECORATOR   │ 🎁 Gift wrapping     │ Add features dynamically
 *  FACADE      │ 🏠 Front door        │ Simplify complex subsystem
 *  FLYWEIGHT   │ 🪶 Share memory      │ Many similar objects
 *  PROXY       │ 🛡️ Bodyguard        │ Control access, lazy loading
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * MNEMONICS TO REMEMBER:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *  "ABCD-FFP" (All Big Companies Deliver Fast Food & Pizza)
 *  
 *     A - Adapter    (plug converter)
 *     B - Bridge     (separate what/how)
 *     C - Composite  (tree)
 *     D - Decorator  (wrap gifts)
 *     F - Facade     (front door)
 *     F - Flyweight  (feather light)
 *     P - Proxy      (protection)
 */
