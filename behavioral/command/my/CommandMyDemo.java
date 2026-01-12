/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * COMMAND PATTERN - Explained Like You're a Kid! 🧒
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * STORY TIME! 📖
 * ──────────────
 * Imagine you're at a RESTAURANT 🍽️
 * 
 * WITHOUT Command Pattern:
 *   - You go directly to kitchen and tell chef "Make pizza!"
 *   - Chef is busy, confused, orders get mixed up
 *   
 * WITH Command Pattern:
 *   - You tell WAITER your order
 *   - Waiter writes ORDER SLIP 📝 (this is the COMMAND!)
 *   - Waiter gives slip to kitchen
 *   - Kitchen executes when ready
 *   - Can CANCEL order if needed!
 *   
 *       👤 Customer          📝 Order Slip         👨‍🍳 Kitchen
 *      (Invoker)            (Command)            (Receiver)
 *          │                    │                    │
 *          │─── "I want pizza" ─┼───────────────────→│
 *          │    (writes slip)   │     (executes)     │
 * 
 * THE PATTERN:
 * ─────────────
 *   - Wrap a REQUEST as an OBJECT (the command)
 *   - Can store, queue, undo, redo commands
 *   - Decouples "what to do" from "when to do it"
 */

import java.util.Stack;
import java.util.ArrayList;
import java.util.List;

// ═══════════════════════════════════════════════════════════════════════════════
// STEP 1: Command Interface
// ═══════════════════════════════════════════════════════════════════════════════
interface Command {
    void execute();
    void undo();
    String getDescription();
}

// ═══════════════════════════════════════════════════════════════════════════════
// STEP 2: Receiver (the thing that does actual work)
// ═══════════════════════════════════════════════════════════════════════════════
class Light {
    private String location;
    private boolean isOn = false;
    
    public Light(String location) {
        this.location = location;
    }
    
    public void turnOn() {
        isOn = true;
        System.out.println("  💡 " + location + " light is ON");
    }
    
    public void turnOff() {
        isOn = false;
        System.out.println("  💡 " + location + " light is OFF");
    }
    
    public boolean isOn() { return isOn; }
}

class Fan {
    private String location;
    private int speed = 0;  // 0=off, 1=low, 2=medium, 3=high
    
    public Fan(String location) {
        this.location = location;
    }
    
    public void setSpeed(int speed) {
        this.speed = speed;
        String[] levels = {"OFF", "LOW", "MEDIUM", "HIGH"};
        System.out.println("  🌀 " + location + " fan is " + levels[speed]);
    }
    
    public int getSpeed() { return speed; }
}

// ═══════════════════════════════════════════════════════════════════════════════
// STEP 3: Concrete Commands (the order slips!)
// ═══════════════════════════════════════════════════════════════════════════════
class LightOnCommand implements Command {
    private Light light;
    
    public LightOnCommand(Light light) {
        this.light = light;
    }
    
    public void execute() {
        light.turnOn();
    }
    
    public void undo() {
        light.turnOff();
    }
    
    public String getDescription() { return "Turn light ON"; }
}

class LightOffCommand implements Command {
    private Light light;
    
    public LightOffCommand(Light light) {
        this.light = light;
    }
    
    public void execute() {
        light.turnOff();
    }
    
    public void undo() {
        light.turnOn();
    }
    
    public String getDescription() { return "Turn light OFF"; }
}

class FanHighCommand implements Command {
    private Fan fan;
    private int previousSpeed;
    
    public FanHighCommand(Fan fan) {
        this.fan = fan;
    }
    
    public void execute() {
        previousSpeed = fan.getSpeed();  // Save for undo
        fan.setSpeed(3);
    }
    
    public void undo() {
        fan.setSpeed(previousSpeed);
    }
    
    public String getDescription() { return "Set fan to HIGH"; }
}

class FanOffCommand implements Command {
    private Fan fan;
    private int previousSpeed;
    
    public FanOffCommand(Fan fan) {
        this.fan = fan;
    }
    
    public void execute() {
        previousSpeed = fan.getSpeed();
        fan.setSpeed(0);
    }
    
    public void undo() {
        fan.setSpeed(previousSpeed);
    }
    
    public String getDescription() { return "Turn fan OFF"; }
}

// Macro Command - executes multiple commands!
class MacroCommand implements Command {
    private Command[] commands;
    private String name;
    
    public MacroCommand(String name, Command[] commands) {
        this.name = name;
        this.commands = commands;
    }
    
    public void execute() {
        for (Command cmd : commands) {
            cmd.execute();
        }
    }
    
    public void undo() {
        // Undo in reverse order!
        for (int i = commands.length - 1; i >= 0; i--) {
            commands[i].undo();
        }
    }
    
    public String getDescription() { return name; }
}

// ═══════════════════════════════════════════════════════════════════════════════
// STEP 4: Invoker (the waiter - holds and invokes commands)
// ═══════════════════════════════════════════════════════════════════════════════
class RemoteControl {
    private Command[] onCommands;
    private Command[] offCommands;
    private Stack<Command> undoStack = new Stack<>();
    
    public RemoteControl(int slots) {
        onCommands = new Command[slots];
        offCommands = new Command[slots];
    }
    
    public void setCommand(int slot, Command onCmd, Command offCmd) {
        onCommands[slot] = onCmd;
        offCommands[slot] = offCmd;
    }
    
    public void pressOn(int slot) {
        if (onCommands[slot] != null) {
            System.out.println("▶️ Executing: " + onCommands[slot].getDescription());
            onCommands[slot].execute();
            undoStack.push(onCommands[slot]);
        }
    }
    
    public void pressOff(int slot) {
        if (offCommands[slot] != null) {
            System.out.println("▶️ Executing: " + offCommands[slot].getDescription());
            offCommands[slot].execute();
            undoStack.push(offCommands[slot]);
        }
    }
    
    public void pressUndo() {
        if (!undoStack.isEmpty()) {
            Command lastCommand = undoStack.pop();
            System.out.println("↩️ Undoing: " + lastCommand.getDescription());
            lastCommand.undo();
        } else {
            System.out.println("Nothing to undo!");
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// DEMO
// ═══════════════════════════════════════════════════════════════════════════════
public class CommandMyDemo {
    
    public static void main(String[] args) {
        System.out.println("═══ COMMAND PATTERN - SMART HOME REMOTE ═══\n");
        
        // Create receivers (actual devices)
        Light livingRoomLight = new Light("Living Room");
        Light bedroomLight = new Light("Bedroom");
        Fan ceilingFan = new Fan("Ceiling");
        
        // Create commands (order slips)
        Command livingLightOn = new LightOnCommand(livingRoomLight);
        Command livingLightOff = new LightOffCommand(livingRoomLight);
        Command bedroomLightOn = new LightOnCommand(bedroomLight);
        Command bedroomLightOff = new LightOffCommand(bedroomLight);
        Command fanHigh = new FanHighCommand(ceilingFan);
        Command fanOff = new FanOffCommand(ceilingFan);
        
        // Create invoker (remote control)
        RemoteControl remote = new RemoteControl(3);
        remote.setCommand(0, livingLightOn, livingLightOff);
        remote.setCommand(1, bedroomLightOn, bedroomLightOff);
        remote.setCommand(2, fanHigh, fanOff);
        
        // Use the remote
        System.out.println("─── Using Remote Control ───\n");
        
        remote.pressOn(0);   // Living room light ON
        remote.pressOn(1);   // Bedroom light ON
        remote.pressOn(2);   // Fan HIGH
        
        System.out.println();
        remote.pressUndo();  // Undo fan
        remote.pressUndo();  // Undo bedroom light
        
        System.out.println("\n─── Macro Command (Party Mode!) ───\n");
        
        // Macro command - multiple commands at once!
        Command[] partyOn = { livingLightOn, bedroomLightOn, fanHigh };
        Command[] partyOff = { livingLightOff, bedroomLightOff, fanOff };
        
        MacroCommand partyModeOn = new MacroCommand("🎉 Party Mode ON", partyOn);
        MacroCommand partyModeOff = new MacroCommand("😴 Party Mode OFF", partyOff);
        
        remote.setCommand(0, partyModeOn, partyModeOff);
        
        System.out.println("▶️ Activating: 🎉 Party Mode ON");
        partyModeOn.execute();
        
        System.out.println("\n↩️ Undoing Party Mode:");
        partyModeOn.undo();
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * WITHOUT COMMAND (BAD - Tight coupling):
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     class RemoteControl {
 *         Light light;
 *         Fan fan;
 *         
 *         void pressButton1() {
 *             light.turnOn();  // Directly calls light!
 *         }
 *         
 *         void pressButton2() {
 *             fan.setSpeed(3);  // Directly calls fan!
 *         }
 *         
 *         // Can't undo!
 *         // Can't queue commands!
 *         // Can't do macros!
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * WITH COMMAND (GOOD - Decoupled):
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     - Remote doesn't know about Light or Fan
 *     - Just executes Command objects
 *     - Can UNDO any command
 *     - Can QUEUE commands
 *     - Can create MACRO commands
 *     - Can LOG all commands
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * REAL-WORLD EXAMPLES:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     1. TEXT EDITOR (Ctrl+Z / Ctrl+Y)
 *        - Each action (type, delete, paste) is a Command
 *        - Undo/Redo stack of commands
 *     
 *     2. RESTAURANT ORDER SYSTEM
 *        - Order slip = Command
 *        - Waiter = Invoker
 *        - Kitchen = Receiver
 *     
 *     3. TRANSACTION SYSTEM
 *        - Bank transactions as commands
 *        - Can rollback (undo) if fails
 *     
 *     4. TASK SCHEDULER
 *        - Jobs queued as commands
 *        - Execute when resources available
 *     
 *     5. GUI BUTTONS
 *        - Button click → Execute command
 *        - Same command can be triggered by menu or shortcut
 *     
 *     6. GAME (Move commands)
 *        - Player moves as commands
 *        - Can replay game from command history
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * KEY BENEFITS:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     ✅ UNDO/REDO - Each command knows how to reverse itself
 *     ✅ QUEUE - Store commands for later execution
 *     ✅ LOG - Record all commands for audit/replay
 *     ✅ MACRO - Combine multiple commands into one
 *     ✅ DECOUPLE - Invoker doesn't know about receivers
 */
