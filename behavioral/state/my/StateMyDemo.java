/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * STATE PATTERN - Explained Like You're a Kid! 🧒
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * STORY TIME! 📖
 * ──────────────
 * Imagine a TRAFFIC LIGHT 🚦
 * 
 * It has 3 STATES: 🔴 RED, 🟡 YELLOW, 🟢 GREEN
 * 
 * The SAME button "change" does DIFFERENT things based on current state:
 *   - If RED    → change → becomes GREEN
 *   - If GREEN  → change → becomes YELLOW  
 *   - If YELLOW → change → becomes RED
 * 
 * THE PROBLEM (without State pattern):
 * ────────────────────────────────────
 *     void change() {
 *         if (state == "RED") { state = "GREEN"; }
 *         else if (state == "GREEN") { state = "YELLOW"; }
 *         else if (state == "YELLOW") { state = "RED"; }
 *     }
 *     
 *     // UGLY! Gets worse with more states and actions!
 * 
 * THE SOLUTION (State pattern):
 * ──────────────────────────────
 *     Each state is a SEPARATE CLASS that knows:
 *       - What to DO in this state
 *       - What state comes NEXT
 */

// ═══════════════════════════════════════════════════════════════════════════════
// STEP 1: State Interface
// ═══════════════════════════════════════════════════════════════════════════════
interface TrafficLightState {
    void change(TrafficLight light);
    String getColor();
}

// ═══════════════════════════════════════════════════════════════════════════════
// STEP 2: Concrete States
// ═══════════════════════════════════════════════════════════════════════════════
class RedState implements TrafficLightState {
    
    public String getColor() { 
        return "🔴 RED - STOP!"; 
    }
    
    public void change(TrafficLight light) {
        System.out.println("  Changing from RED → GREEN");
        light.setState(new GreenState());  // Next state
    }
}

class GreenState implements TrafficLightState {
    
    public String getColor() { 
        return "🟢 GREEN - GO!"; 
    }
    
    public void change(TrafficLight light) {
        System.out.println("  Changing from GREEN → YELLOW");
        light.setState(new YellowState());  // Next state
    }
}

class YellowState implements TrafficLightState {
    
    public String getColor() { 
        return "🟡 YELLOW - SLOW DOWN!"; 
    }
    
    public void change(TrafficLight light) {
        System.out.println("  Changing from YELLOW → RED");
        light.setState(new RedState());  // Next state
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// STEP 3: Context (the thing that HAS state)
// ═══════════════════════════════════════════════════════════════════════════════
class TrafficLight {
    private TrafficLightState state;
    
    public TrafficLight() {
        this.state = new RedState();  // Start with RED
    }
    
    public void setState(TrafficLightState state) {
        this.state = state;
    }
    
    public void change() {
        state.change(this);  // Delegate to current state
    }
    
    public void showCurrentState() {
        System.out.println("Current: " + state.getColor());
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// DEMO
// ═══════════════════════════════════════════════════════════════════════════════
public class StateMyDemo {
    
    public static void main(String[] args) {
        System.out.println("═══ STATE PATTERN - TRAFFIC LIGHT ═══\n");
        
        TrafficLight light = new TrafficLight();
        
        // Cycle through states
        for (int i = 0; i < 6; i++) {
            light.showCurrentState();
            light.change();
            System.out.println();
        }
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * WITHOUT STATE PATTERN (BAD - lots of if-else):
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     class TrafficLight {
 *         String state = "RED";
 *         
 *         void change() {
 *             if (state.equals("RED")) {
 *                 state = "GREEN";
 *             } else if (state.equals("GREEN")) {
 *                 state = "YELLOW";
 *             } else if (state.equals("YELLOW")) {
 *                 state = "RED";
 *             }
 *         }
 *         
 *         void doAction() {
 *             if (state.equals("RED")) {
 *                 System.out.println("STOP!");
 *             } else if (state.equals("GREEN")) {
 *                 System.out.println("GO!");
 *             } else if (state.equals("YELLOW")) {
 *                 System.out.println("SLOW DOWN!");
 *             }
 *         }
 *         
 *         // Every method has these ugly if-else chains!
 *         // Adding new state = modify EVERY method!
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * WITH STATE PATTERN (GOOD):
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     - Each state is its OWN class
 *     - No if-else chains
 *     - Adding new state = just add one new class
 *     - Each state knows its own behavior
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * STATE vs STRATEGY - What's the difference?
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     STRATEGY:
 *     - Client chooses which strategy to use
 *     - Strategies don't know about each other
 *     - "I want to pay with credit card"
 *     
 *     STATE:
 *     - States change THEMSELVES automatically
 *     - Each state knows the NEXT state
 *     - "Light is RED, when changed it becomes GREEN"
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * REAL-WORLD EXAMPLES:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     - Vending Machine: Idle → HasMoney → Dispensing → Idle
 *     - Order Status: Pending → Confirmed → Shipped → Delivered
 *     - Document: Draft → Review → Approved → Published
 *     - TCP Connection: Closed → Listen → Established → Closed
 *     - Player in Game: Idle → Running → Jumping → Falling → Idle
 */
