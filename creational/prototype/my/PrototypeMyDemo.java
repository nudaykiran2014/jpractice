/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * PROTOTYPE PATTERN - Explained Like You're a Kid! 🧒
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * STORY TIME! 📖
 * ──────────────
 * Imagine you're making PHOTOCOPIES 📄
 * 
 * WITHOUT Prototype:
 *   - Write the whole document again from scratch
 *   - Takes a long time! ⏰
 *   
 * WITH Prototype:
 *   - Put document in photocopier
 *   - Press COPY → Get exact duplicate! 📄📄
 *   - Make small changes if needed
 * 
 *       Original         Clone
 *         📄  ──COPY──→  📄
 *        (prototype)    (new object)
 * 
 * THE PATTERN:
 * ─────────────
 *   - Create new objects by COPYING existing ones
 *   - Instead of: new Object() with complex setup
 *   - Just call: existingObject.clone()
 *   
 *   Perfect when:
 *   - Creating object is EXPENSIVE (database calls, API calls)
 *   - Objects have many configurations
 *   - You want variations of same base object
 */

import java.util.HashMap;
import java.util.Map;

// ═══════════════════════════════════════════════════════════════════════════════
// STEP 1: Prototype Interface
// ═══════════════════════════════════════════════════════════════════════════════
interface GameCharacter extends Cloneable {
    GameCharacter clone();
    void showStats();
}

// ═══════════════════════════════════════════════════════════════════════════════
// STEP 2: Concrete Prototypes
// ═══════════════════════════════════════════════════════════════════════════════
class Warrior implements GameCharacter {
    private String name;
    private int health;
    private int attack;
    private int defense;
    private String weapon;
    
    public Warrior(String name, int health, int attack, int defense, String weapon) {
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.defense = defense;
        this.weapon = weapon;
        
        // Simulate expensive creation (loading from DB, API, etc.)
        System.out.println("  ⏳ Creating Warrior from scratch (expensive!)");
        try { Thread.sleep(100); } catch (Exception e) {}
    }
    
    // Clone method - creates copy WITHOUT expensive operations!
    public Warrior clone() {
        System.out.println("  ⚡ Cloning Warrior (fast!)");
        Warrior clone = new Warrior();
        clone.name = this.name;
        clone.health = this.health;
        clone.attack = this.attack;
        clone.defense = this.defense;
        clone.weapon = this.weapon;
        return clone;
    }
    
    // Private constructor for cloning (skips expensive setup)
    private Warrior() {}
    
    public void setName(String name) { this.name = name; }
    public void setWeapon(String weapon) { this.weapon = weapon; }
    
    public void showStats() {
        System.out.println("  ⚔️ " + name + " | HP:" + health + " ATK:" + attack + 
            " DEF:" + defense + " | Weapon: " + weapon);
    }
}

class Mage implements GameCharacter {
    private String name;
    private int health;
    private int mana;
    private int magicPower;
    private String spell;
    
    public Mage(String name, int health, int mana, int magicPower, String spell) {
        this.name = name;
        this.health = health;
        this.mana = mana;
        this.magicPower = magicPower;
        this.spell = spell;
        
        System.out.println("  ⏳ Creating Mage from scratch (expensive!)");
        try { Thread.sleep(100); } catch (Exception e) {}
    }
    
    public Mage clone() {
        System.out.println("  ⚡ Cloning Mage (fast!)");
        Mage clone = new Mage();
        clone.name = this.name;
        clone.health = this.health;
        clone.mana = this.mana;
        clone.magicPower = this.magicPower;
        clone.spell = this.spell;
        return clone;
    }
    
    private Mage() {}
    
    public void setName(String name) { this.name = name; }
    public void setSpell(String spell) { this.spell = spell; }
    
    public void showStats() {
        System.out.println("  🧙 " + name + " | HP:" + health + " MP:" + mana + 
            " PWR:" + magicPower + " | Spell: " + spell);
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// STEP 3: Prototype Registry (Optional - stores pre-made prototypes)
// ═══════════════════════════════════════════════════════════════════════════════
class CharacterRegistry {
    private Map<String, GameCharacter> prototypes = new HashMap<>();
    
    public void register(String key, GameCharacter prototype) {
        prototypes.put(key, prototype);
    }
    
    public GameCharacter create(String key) {
        GameCharacter prototype = prototypes.get(key);
        if (prototype != null) {
            return prototype.clone();
        }
        return null;
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// DEMO
// ═══════════════════════════════════════════════════════════════════════════════
public class PrototypeMyDemo {
    
    public static void main(String[] args) {
        System.out.println("═══ PROTOTYPE PATTERN - GAME CHARACTERS ═══\n");
        
        // ─────────────────────────────────────────────────────────────────────
        // WITHOUT PROTOTYPE: Create each character from scratch
        // ─────────────────────────────────────────────────────────────────────
        System.out.println("❌ WITHOUT Prototype (Creating 3 warriors from scratch):");
        System.out.println("───────────────────────────────────────────────────────");
        
        long start = System.currentTimeMillis();
        Warrior w1 = new Warrior("Knight1", 100, 20, 15, "Sword");
        Warrior w2 = new Warrior("Knight2", 100, 20, 15, "Sword");
        Warrior w3 = new Warrior("Knight3", 100, 20, 15, "Sword");
        long end = System.currentTimeMillis();
        
        System.out.println("  Time taken: " + (end - start) + "ms\n");
        
        // ─────────────────────────────────────────────────────────────────────
        // WITH PROTOTYPE: Create once, clone many times
        // ─────────────────────────────────────────────────────────────────────
        System.out.println("✅ WITH Prototype (Create 1, clone 2):");
        System.out.println("───────────────────────────────────────────────────────");
        
        start = System.currentTimeMillis();
        Warrior baseWarrior = new Warrior("BaseKnight", 100, 20, 15, "Sword");
        Warrior clone1 = baseWarrior.clone();
        clone1.setName("Knight1");
        Warrior clone2 = baseWarrior.clone();
        clone2.setName("Knight2");
        clone2.setWeapon("Axe");  // Customize the clone!
        end = System.currentTimeMillis();
        
        System.out.println("  Time taken: " + (end - start) + "ms\n");
        
        System.out.println("📋 Cloned Characters:");
        baseWarrior.showStats();
        clone1.showStats();
        clone2.showStats();
        
        // ─────────────────────────────────────────────────────────────────────
        // Using Prototype Registry
        // ─────────────────────────────────────────────────────────────────────
        System.out.println("\n\n📚 Using Prototype Registry:");
        System.out.println("───────────────────────────────────────────────────────");
        
        // Pre-create prototypes (one-time setup)
        CharacterRegistry registry = new CharacterRegistry();
        registry.register("warrior", new Warrior("Warrior", 100, 20, 15, "Sword"));
        registry.register("mage", new Mage("Mage", 60, 100, 30, "Fireball"));
        
        System.out.println("\n🎮 Creating characters from registry:");
        
        // Now create many characters quickly!
        GameCharacter player1 = registry.create("warrior");
        GameCharacter player2 = registry.create("mage");
        GameCharacter enemy1 = registry.create("warrior");
        GameCharacter enemy2 = registry.create("mage");
        
        System.out.println("\n📋 All Characters:");
        player1.showStats();
        player2.showStats();
        enemy1.showStats();
        enemy2.showStats();
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * SHALLOW vs DEEP CLONE:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     SHALLOW CLONE:
 *     ──────────────
 *         - Copies primitive values
 *         - References to objects are SHARED
 *         
 *         Original: name="John", address=📍0x123
 *         Clone:    name="John", address=📍0x123  ← Same address object!
 *     
 *     DEEP CLONE:
 *     ───────────
 *         - Copies primitive values
 *         - Creates NEW copies of referenced objects
 *         
 *         Original: name="John", address=📍0x123
 *         Clone:    name="John", address=📍0x456  ← New address object!
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * JAVA'S CLONEABLE:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     class Person implements Cloneable {
 *         String name;
 *         
 *         @Override
 *         protected Object clone() throws CloneNotSupportedException {
 *             return super.clone();  // Shallow clone
 *         }
 *     }
 *     
 *     Person p1 = new Person("John");
 *     Person p2 = (Person) p1.clone();
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * REAL-WORLD EXAMPLES:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     1. GAME DEVELOPMENT
 *        - Clone enemy NPCs with same stats
 *        - Clone level templates
 *     
 *     2. DOCUMENT EDITORS
 *        - "Duplicate" feature
 *        - Copy slide in PowerPoint
 *     
 *     3. DATABASE RECORDS
 *        - Clone a record as template
 *        - "Copy from previous" feature
 *     
 *     4. UI COMPONENTS
 *        - Clone styled buttons
 *        - Duplicate form fields
 *     
 *     5. CONFIGURATION OBJECTS
 *        - Base config + slight variations
 *        - Dev/Staging/Prod configs from base
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * PROTOTYPE vs FACTORY:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     FACTORY:
 *        - Creates NEW objects from scratch
 *        - Uses constructors
 *        - Good when objects are simple to create
 *     
 *     PROTOTYPE:
 *        - Creates objects by CLONING existing ones
 *        - Uses clone() method
 *        - Good when creation is EXPENSIVE
 */
