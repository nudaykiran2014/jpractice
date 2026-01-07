/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * FLYWEIGHT PATTERN - Explained Like You're a Kid! 🧒
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * STORY TIME! 📖
 * ──────────────
 * Imagine you're making a FOREST in a video game 🌲🌲🌲
 * 
 * WITHOUT Flyweight:
 *   - 10,000 trees
 *   - Each tree stores: texture (5MB), mesh (2MB), color, position
 *   - Total: 10,000 × 7MB = 70 GB! 💥 CRASH!
 *   
 * WITH Flyweight:
 *   - Tree TYPES: Oak (texture+mesh), Pine (texture+mesh) = 2 types
 *   - 10,000 trees just store: type reference + position
 *   - Total: 2 × 7MB + 10,000 × tiny = ~14 MB! ✅
 *   
 *     Tree1 (x:10, y:20) ──┐
 *     Tree2 (x:30, y:40) ──┼──→ 🌲 OakType (texture, mesh) ← SHARED!
 *     Tree3 (x:50, y:60) ──┘
 *     
 *     Tree4 (x:15, y:25) ──┐
 *     Tree5 (x:35, y:45) ──┼──→ 🌲 PineType (texture, mesh) ← SHARED!
 *     Tree6 (x:55, y:65) ──┘
 * 
 * THE PATTERN:
 * ─────────────
 *     INTRINSIC STATE (shared, immutable):
 *        - Texture, mesh, color scheme
 *        - Same for all trees of same type
 *        
 *     EXTRINSIC STATE (unique, per instance):
 *        - Position (x, y)
 *        - Health, age
 *        - Different for each tree
 */

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

// ═══════════════════════════════════════════════════════════════════════════════
// FLYWEIGHT - Shared intrinsic state
// ═══════════════════════════════════════════════════════════════════════════════
class TreeType {
    private String name;
    private String color;
    private String texture;  // Imagine this is 5MB of data!
    
    public TreeType(String name, String color, String texture) {
        this.name = name;
        this.color = color;
        this.texture = texture;
        // Simulate expensive resource loading
        System.out.println("  🌲 Loading TreeType: " + name + " (expensive - 5MB texture!)");
    }
    
    public void draw(int x, int y) {
        System.out.println("  Drawing " + color + " " + name + " at (" + x + ", " + y + ")");
    }
    
    public String getName() { return name; }
}

// ═══════════════════════════════════════════════════════════════════════════════
// FLYWEIGHT FACTORY - Creates and manages flyweights
// ═══════════════════════════════════════════════════════════════════════════════
class TreeFactory {
    private static Map<String, TreeType> treeTypes = new HashMap<>();
    
    public static TreeType getTreeType(String name, String color, String texture) {
        String key = name + "_" + color;
        
        if (!treeTypes.containsKey(key)) {
            // Create only if doesn't exist
            treeTypes.put(key, new TreeType(name, color, texture));
        } else {
            System.out.println("  ♻️ Reusing existing TreeType: " + name);
        }
        
        return treeTypes.get(key);
    }
    
    public static int getTotalTypesCreated() {
        return treeTypes.size();
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// CONTEXT - Uses flyweight + stores extrinsic state
// ═══════════════════════════════════════════════════════════════════════════════
class Tree {
    private int x;           // Extrinsic - unique per tree
    private int y;           // Extrinsic - unique per tree
    private TreeType type;   // Intrinsic - SHARED!
    
    public Tree(int x, int y, TreeType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }
    
    public void draw() {
        type.draw(x, y);
    }
}

// Forest that manages many trees
class Forest {
    private List<Tree> trees = new ArrayList<>();
    
    public void plantTree(int x, int y, String name, String color, String texture) {
        TreeType type = TreeFactory.getTreeType(name, color, texture);
        trees.add(new Tree(x, y, type));
    }
    
    public void drawForest() {
        System.out.println("\n🌳 Drawing forest with " + trees.size() + " trees:");
        for (Tree tree : trees) {
            tree.draw();
        }
    }
    
    public int getTreeCount() { return trees.size(); }
}

// ═══════════════════════════════════════════════════════════════════════════════
// REAL-WORLD EXAMPLE: Text Editor Characters
// ═══════════════════════════════════════════════════════════════════════════════

// Flyweight - Character style (shared)
class CharacterStyle {
    private String font;
    private int size;
    private String color;
    
    public CharacterStyle(String font, int size, String color) {
        this.font = font;
        this.size = size;
        this.color = color;
        System.out.println("  📝 Created style: " + font + "-" + size + "-" + color);
    }
    
    public void render(char c, int position) {
        System.out.println("  '" + c + "' at pos " + position + 
            " [" + font + ", " + size + "px, " + color + "]");
    }
}

// Flyweight Factory for styles
class StyleFactory {
    private static Map<String, CharacterStyle> styles = new HashMap<>();
    
    public static CharacterStyle getStyle(String font, int size, String color) {
        String key = font + "-" + size + "-" + color;
        
        if (!styles.containsKey(key)) {
            styles.put(key, new CharacterStyle(font, size, color));
        }
        return styles.get(key);
    }
    
    public static int getTotalStyles() { return styles.size(); }
}

// Context - Document character
class DocumentCharacter {
    private char character;          // Extrinsic
    private int position;            // Extrinsic
    private CharacterStyle style;    // Intrinsic - SHARED!
    
    public DocumentCharacter(char c, int pos, CharacterStyle style) {
        this.character = c;
        this.position = pos;
        this.style = style;
    }
    
    public void render() {
        style.render(character, position);
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// DEMO
// ═══════════════════════════════════════════════════════════════════════════════
public class FlyweightMyDemo {
    
    public static void main(String[] args) {
        System.out.println("═══ FLYWEIGHT PATTERN - GAME FOREST ═══\n");
        
        Forest forest = new Forest();
        
        System.out.println("Planting trees (watch how types are reused!):\n");
        
        // Plant many trees - only 2 tree types will be created!
        forest.plantTree(10, 20, "Oak", "Green", "oak_texture.png");
        forest.plantTree(30, 40, "Pine", "DarkGreen", "pine_texture.png");
        forest.plantTree(50, 60, "Oak", "Green", "oak_texture.png");      // Reused!
        forest.plantTree(70, 80, "Oak", "Green", "oak_texture.png");      // Reused!
        forest.plantTree(90, 100, "Pine", "DarkGreen", "pine_texture.png"); // Reused!
        forest.plantTree(110, 120, "Oak", "Green", "oak_texture.png");    // Reused!
        
        System.out.println("\n📊 Statistics:");
        System.out.println("  Total trees planted: " + forest.getTreeCount());
        System.out.println("  Tree types in memory: " + TreeFactory.getTotalTypesCreated());
        System.out.println("  Memory saved: " + (forest.getTreeCount() - TreeFactory.getTotalTypesCreated()) + " texture duplicates avoided!");
        
        forest.drawForest();
        
        System.out.println("\n\n═══ FLYWEIGHT PATTERN - TEXT EDITOR ═══\n");
        
        System.out.println("Creating document characters:\n");
        
        List<DocumentCharacter> document = new ArrayList<>();
        
        // Create document - many chars but few styles
        String text = "Hello";
        for (int i = 0; i < text.length(); i++) {
            CharacterStyle style = StyleFactory.getStyle("Arial", 12, "black");
            document.add(new DocumentCharacter(text.charAt(i), i, style));
        }
        
        // Add bold text
        String boldText = "World";
        for (int i = 0; i < boldText.length(); i++) {
            CharacterStyle style = StyleFactory.getStyle("Arial-Bold", 12, "black");
            document.add(new DocumentCharacter(boldText.charAt(i), text.length() + i, style));
        }
        
        System.out.println("\n📊 Statistics:");
        System.out.println("  Total characters: " + document.size());
        System.out.println("  Unique styles: " + StyleFactory.getTotalStyles());
        
        System.out.println("\n📄 Rendering document:");
        for (DocumentCharacter dc : document) {
            dc.render();
        }
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * WITHOUT FLYWEIGHT (BAD - Memory explosion):
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     class Tree {
 *         int x, y;
 *         String texture;      // 5MB each!
 *         String mesh;         // 2MB each!
 *         String color;
 *     }
 *     
 *     // 10,000 trees = 10,000 × 7MB = 70GB! 💥
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * WITH FLYWEIGHT (GOOD - Share common data):
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     class TreeType {         // Shared (2 types = 14MB)
 *         String texture;
 *         String mesh;
 *     }
 *     
 *     class Tree {             // Per tree (10,000 × ~16 bytes)
 *         int x, y;
 *         TreeType type;       // Reference only!
 *     }
 *     
 *     // 10,000 trees = 14MB + ~160KB = ~14MB! ✅
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * REAL-WORLD EXAMPLES:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     1. GAME DEVELOPMENT
 *        - Trees, bullets, particles share textures
 *     
 *     2. TEXT EDITORS
 *        - Characters share font/style objects
 *     
 *     3. JAVA STRING POOL
 *        - String literals are interned (shared)
 *     
 *     4. BROWSER ICONS
 *        - Same icon image shared across tabs
 *     
 *     5. DATABASE CONNECTION POOL
 *        - Connections reused instead of created
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * KEY CONCEPTS:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     INTRINSIC STATE:
 *        - Shared among all instances
 *        - Immutable (cannot change)
 *        - Stored in flyweight
 *        
 *     EXTRINSIC STATE:
 *        - Unique per instance
 *        - Can change
 *        - Passed to flyweight methods
 */
