/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * VISITOR PATTERN - Explained Like You're a Kid! 🧒
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * STORY TIME! 📖
 * ──────────────
 * Imagine a ZOO with different animals: 🦁 Lion, 🐘 Elephant, 🐒 Monkey
 * 
 * Now different VISITORS come to the zoo:
 *   - 👨‍⚕️ Doctor (checks health of each animal differently)
 *   - 🍎 Feeder (feeds each animal different food)
 * 
 * Each visitor does DIFFERENT things to DIFFERENT animals!
 * 
 *     Doctor visits Lion    → Checks teeth
 *     Doctor visits Elephant → Checks trunk
 *     Doctor visits Monkey  → Checks tail
 *     
 *     Feeder visits Lion    → Gives meat
 *     Feeder visits Elephant → Gives bananas
 *     Feeder visits Monkey  → Gives fruits
 * 
 * THE PATTERN:
 * ─────────────
 *     Animal says: "Hey visitor, come visit ME!"
 *     Visitor says: "Okay, I'll do my special thing for YOU!"
 */

import java.util.ArrayList;
import java.util.List;

// ═══════════════════════════════════════════════════════════════════════════════
// STEP 1: The Visitor Interface (what visitors can do)
// ═══════════════════════════════════════════════════════════════════════════════
interface AnimalVisitor {
    void visit(Lion lion);
    void visit(Elephant elephant);
    void visit(Monkey monkey);
}

// ═══════════════════════════════════════════════════════════════════════════════
// STEP 2: The Element Interface (animals that can be visited)
// ═══════════════════════════════════════════════════════════════════════════════
interface Animal {
    void accept(AnimalVisitor visitor);  // "Come visit me!"
    String getName();
}

// ═══════════════════════════════════════════════════════════════════════════════
// STEP 3: Concrete Animals
// ═══════════════════════════════════════════════════════════════════════════════
class Lion implements Animal {
    public String getName() { return "🦁 Lion"; }
    
    public void accept(AnimalVisitor visitor) {
        visitor.visit(this);  // "Visitor, do your thing on ME (a Lion)!"
    }
}

class Elephant implements Animal {
    public String getName() { return "🐘 Elephant"; }
    
    public void accept(AnimalVisitor visitor) {
        visitor.visit(this);  // "Visitor, do your thing on ME (an Elephant)!"
    }
}

class Monkey implements Animal {
    public String getName() { return "🐒 Monkey"; }
    
    public void accept(AnimalVisitor visitor) {
        visitor.visit(this);  // "Visitor, do your thing on ME (a Monkey)!"
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// STEP 4: Concrete Visitors (each does different things!)
// ═══════════════════════════════════════════════════════════════════════════════

// 👨‍⚕️ Doctor Visitor - checks health
class DoctorVisitor implements AnimalVisitor {
    
    public void visit(Lion lion) {
        System.out.println("👨‍⚕️ Doctor checks " + lion.getName() + "'s TEETH 🦷");
    }
    
    public void visit(Elephant elephant) {
        System.out.println("👨‍⚕️ Doctor checks " + elephant.getName() + "'s TRUNK 👃");
    }
    
    public void visit(Monkey monkey) {
        System.out.println("👨‍⚕️ Doctor checks " + monkey.getName() + "'s TAIL 🐒");
    }
}

// 🍎 Feeder Visitor - feeds animals
class FeederVisitor implements AnimalVisitor {
    
    public void visit(Lion lion) {
        System.out.println("🍖 Feeder gives " + lion.getName() + " some MEAT");
    }
    
    public void visit(Elephant elephant) {
        System.out.println("🍌 Feeder gives " + elephant.getName() + " some BANANAS");
    }
    
    public void visit(Monkey monkey) {
        System.out.println("🍎 Feeder gives " + monkey.getName() + " some FRUITS");
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// The Zoo (holds all animals)
// ═══════════════════════════════════════════════════════════════════════════════
class Zoo {
    private List<Animal> animals = new ArrayList<>();
    
    public void addAnimal(Animal animal) {
        animals.add(animal);
    }
    
    // Let a visitor visit ALL animals
    public void letVisitorIn(AnimalVisitor visitor) {
        for (Animal animal : animals) {
            animal.accept(visitor);
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// DEMO
// ═══════════════════════════════════════════════════════════════════════════════
public class VisitorMyDemo {
    
    public static void main(String[] args) {
        System.out.println("═══ VISITOR PATTERN - ZOO EXAMPLE ═══\n");
        
        // Create the zoo with animals
        Zoo zoo = new Zoo();
        zoo.addAnimal(new Lion());
        zoo.addAnimal(new Elephant());
        zoo.addAnimal(new Monkey());
        
        // Doctor visits all animals
        System.out.println("--- Doctor's Visit ---");
        zoo.letVisitorIn(new DoctorVisitor());
        
        System.out.println();
        
        // Feeder visits all animals
        System.out.println("--- Feeding Time ---");
        zoo.letVisitorIn(new FeederVisitor());
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * WHY IS THIS USEFUL? 🤔
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Want to add a new operation (like "Photographer")?
 *   → Just create PhotoVisitor class! 📸
 *   → NO changes to Animal classes!
 * 
 * WITHOUT Visitor Pattern:
 *   → You'd have to add photo() method to Lion, Elephant, Monkey...
 *   → Every time new operation = change ALL animal classes ❌
 * 
 * WITH Visitor Pattern:
 *   → Animals stay the same
 *   → Just add new Visitor class ✅
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * SIMPLE RULE:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *    "I have THINGS (animals) and OPERATIONS (doctor, feeder).
 *     I want to ADD NEW OPERATIONS without changing the THINGS."
 * 
 *    Animal: "Accept visitor"  →  Visitor: "Do my thing to this animal"
 */