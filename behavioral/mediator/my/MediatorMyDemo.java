/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * MEDIATOR PATTERN - Explained Like You're a Kid! 🧒
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * STORY TIME! 📖
 * ──────────────
 * Imagine an AIRPORT with many PLANES ✈️
 * 
 * WITHOUT Air Traffic Control (Mediator):
 *   - Plane 1 talks to Plane 2, 3, 4, 5...
 *   - Plane 2 talks to Plane 1, 3, 4, 5...
 *   - Everyone talks to everyone = CHAOS! 💥
 *   
 *         ✈️ ←──────→ ✈️
 *          ↖↘      ↙↗
 *            ✈️──✈️
 *          ↙↗      ↖↘
 *         ✈️ ←──────→ ✈️
 * 
 * WITH Air Traffic Control (Mediator):
 *   - All planes talk ONLY to the tower
 *   - Tower coordinates everyone
 *   
 *              ✈️
 *               ↕
 *         ✈️ ↔ 🗼 ↔ ✈️
 *               ↕
 *              ✈️
 * 
 * THE PATTERN:
 * ─────────────
 *   Components don't talk to each other directly.
 *   They talk through a MEDIATOR (central hub).
 */

import java.util.ArrayList;
import java.util.List;

// ═══════════════════════════════════════════════════════════════════════════════
// STEP 1: Mediator Interface
// ═══════════════════════════════════════════════════════════════════════════════
interface ChatMediator {
    void sendMessage(String message, User sender);
    void addUser(User user);
}

// ═══════════════════════════════════════════════════════════════════════════════
// STEP 2: Colleague (User) - talks through mediator
// ═══════════════════════════════════════════════════════════════════════════════
abstract class User {
    protected ChatMediator mediator;
    protected String name;
    
    public User(ChatMediator mediator, String name) {
        this.mediator = mediator;
        this.name = name;
    }
    
    public abstract void send(String message);
    public abstract void receive(String message, String from);
    
    public String getName() { return name; }
}

// ═══════════════════════════════════════════════════════════════════════════════
// STEP 3: Concrete Colleague
// ═══════════════════════════════════════════════════════════════════════════════
class ChatUser extends User {
    
    public ChatUser(ChatMediator mediator, String name) {
        super(mediator, name);
    }
    
    public void send(String message) {
        System.out.println(name + " sends: \"" + message + "\"");
        mediator.sendMessage(message, this);  // Send through mediator!
    }
    
    public void receive(String message, String from) {
        System.out.println("  " + name + " received from " + from + ": \"" + message + "\"");
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// STEP 4: Concrete Mediator (Chat Room)
// ═══════════════════════════════════════════════════════════════════════════════
class ChatRoom implements ChatMediator {
    private List<User> users = new ArrayList<>();
    
    public void addUser(User user) {
        users.add(user);
        System.out.println("📢 " + user.getName() + " joined the chat!");
    }
    
    public void sendMessage(String message, User sender) {
        // Send to ALL users except sender
        for (User user : users) {
            if (user != sender) {
                user.receive(message, sender.getName());
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// DEMO
// ═══════════════════════════════════════════════════════════════════════════════
public class MediatorMyDemo {
    
    public static void main(String[] args) {
        System.out.println("═══ MEDIATOR PATTERN - CHAT ROOM ═══\n");
        
        // Create mediator (chat room)
        ChatMediator chatRoom = new ChatRoom();
        
        // Create users
        User john = new ChatUser(chatRoom, "John");
        User jane = new ChatUser(chatRoom, "Jane");
        User bob = new ChatUser(chatRoom, "Bob");
        
        // Add users to chat room
        chatRoom.addUser(john);
        chatRoom.addUser(jane);
        chatRoom.addUser(bob);
        
        System.out.println();
        
        // Users communicate through mediator
        john.send("Hi everyone!");
        System.out.println();
        
        jane.send("Hey John!");
        System.out.println();
        
        bob.send("Hello all!");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * WITHOUT MEDIATOR (BAD - Tight Coupling):
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     class User {
 *         List<User> contacts;  // Knows about OTHER users!
 *         
 *         void send(String msg) {
 *             for (User u : contacts) {
 *                 u.receive(msg);  // Direct communication
 *             }
 *         }
 *     }
 *     
 *     Problems:
 *     - Every user knows every other user
 *     - Adding new user = update ALL existing users
 *     - Complex web of dependencies
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * WITH MEDIATOR (GOOD - Loose Coupling):
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     class User {
 *         Mediator mediator;  // Only knows mediator!
 *         
 *         void send(String msg) {
 *             mediator.sendMessage(msg, this);  // Through mediator
 *         }
 *     }
 *     
 *     Benefits:
 *     - Users don't know about each other
 *     - Adding new user = just add to mediator
 *     - Single point of control
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * REAL-WORLD EXAMPLES:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     1. AIR TRAFFIC CONTROL
 *        - Planes don't talk to each other
 *        - All communication through tower
 *     
 *     2. CHAT ROOM / WHATSAPP GROUP
 *        - Users send message to room
 *        - Room delivers to all members
 *     
 *     3. STOCK EXCHANGE
 *        - Buyers and sellers don't meet
 *        - Exchange mediates all trades
 *     
 *     4. UI COMPONENTS (Dialog Box)
 *        - Button, TextBox, Checkbox don't know each other
 *        - Dialog mediates: "When checkbox clicked, enable button"
 *     
 *     5. SPRING'S ApplicationContext
 *        - Beans don't know about each other
 *        - Spring container mediates dependencies
 */
