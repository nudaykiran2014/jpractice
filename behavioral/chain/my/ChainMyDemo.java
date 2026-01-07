/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * CHAIN OF RESPONSIBILITY - Explained Like You're a Kid! 🧒
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * STORY TIME! 📖
 * ──────────────
 * Imagine you have a PROBLEM at school 🏫
 * 
 * You first ask your FRIEND → "Can you help?"
 *   - If YES → Problem solved! ✅
 *   - If NO  → Pass to next person...
 *   
 * You ask your TEACHER → "Can you help?"
 *   - If YES → Problem solved! ✅
 *   - If NO  → Pass to next person...
 *   
 * You ask the PRINCIPAL → "Can you help?"
 *   - Principal handles it! ✅
 * 
 * THE CHAIN:
 * ──────────
 *     Request → 👦 Friend → 👩‍🏫 Teacher → 🧑‍💼 Principal
 *                  │            │              │
 *               Can't?       Can't?         Handles!
 *                  ↓            ↓
 *               Pass it      Pass it
 * 
 * THE PATTERN:
 * ─────────────
 *   - Each handler tries to handle the request
 *   - If it CAN'T, it passes to the NEXT handler
 *   - Request travels along the chain until handled
 */

// ═══════════════════════════════════════════════════════════════════════════════
// EXAMPLE: Customer Support System 📞
// ═══════════════════════════════════════════════════════════════════════════════

// Support Ticket
class SupportTicket {
    enum Priority { LOW, MEDIUM, HIGH, CRITICAL }
    
    private String issue;
    private Priority priority;
    
    public SupportTicket(String issue, Priority priority) {
        this.issue = issue;
        this.priority = priority;
    }
    
    public String getIssue() { return issue; }
    public Priority getPriority() { return priority; }
}

// ═══════════════════════════════════════════════════════════════════════════════
// STEP 1: Handler Interface (or Abstract Class)
// ═══════════════════════════════════════════════════════════════════════════════
abstract class SupportHandler {
    protected SupportHandler nextHandler;  // Link to next in chain
    protected String handlerName;
    
    public SupportHandler(String name) {
        this.handlerName = name;
    }
    
    // Set the next handler in chain
    public SupportHandler setNext(SupportHandler next) {
        this.nextHandler = next;
        return next;  // For chaining: a.setNext(b).setNext(c)
    }
    
    // Template method
    public void handle(SupportTicket ticket) {
        if (canHandle(ticket)) {
            processTicket(ticket);
        } else if (nextHandler != null) {
            System.out.println("  " + handlerName + ": Can't handle, passing to next...");
            nextHandler.handle(ticket);
        } else {
            System.out.println("  ❌ No one could handle this ticket!");
        }
    }
    
    protected abstract boolean canHandle(SupportTicket ticket);
    protected abstract void processTicket(SupportTicket ticket);
}

// ═══════════════════════════════════════════════════════════════════════════════
// STEP 2: Concrete Handlers
// ═══════════════════════════════════════════════════════════════════════════════

// Level 1: Bot - handles LOW priority
class BotSupport extends SupportHandler {
    
    public BotSupport() {
        super("🤖 Bot");
    }
    
    protected boolean canHandle(SupportTicket ticket) {
        return ticket.getPriority() == SupportTicket.Priority.LOW;
    }
    
    protected void processTicket(SupportTicket ticket) {
        System.out.println("  " + handlerName + ": Here's an FAQ article for '" + 
            ticket.getIssue() + "' ✅");
    }
}

// Level 2: Junior Agent - handles LOW and MEDIUM
class JuniorAgent extends SupportHandler {
    
    public JuniorAgent() {
        super("👨‍💻 Junior Agent");
    }
    
    protected boolean canHandle(SupportTicket ticket) {
        return ticket.getPriority() == SupportTicket.Priority.LOW ||
               ticket.getPriority() == SupportTicket.Priority.MEDIUM;
    }
    
    protected void processTicket(SupportTicket ticket) {
        System.out.println("  " + handlerName + ": I'll help you with '" + 
            ticket.getIssue() + "' ✅");
    }
}

// Level 3: Senior Agent - handles up to HIGH
class SeniorAgent extends SupportHandler {
    
    public SeniorAgent() {
        super("👩‍💼 Senior Agent");
    }
    
    protected boolean canHandle(SupportTicket ticket) {
        return ticket.getPriority() != SupportTicket.Priority.CRITICAL;
    }
    
    protected void processTicket(SupportTicket ticket) {
        System.out.println("  " + handlerName + ": I'll personally resolve '" + 
            ticket.getIssue() + "' ✅");
    }
}

// Level 4: Manager - handles EVERYTHING including CRITICAL
class Manager extends SupportHandler {
    
    public Manager() {
        super("🧑‍💼 Manager");
    }
    
    protected boolean canHandle(SupportTicket ticket) {
        return true;  // Manager handles everything!
    }
    
    protected void processTicket(SupportTicket ticket) {
        System.out.println("  " + handlerName + ": Top priority! Handling '" + 
            ticket.getIssue() + "' immediately! ✅");
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// DEMO
// ═══════════════════════════════════════════════════════════════════════════════
public class ChainMyDemo {
    
    public static void main(String[] args) {
        System.out.println("═══ CHAIN OF RESPONSIBILITY - SUPPORT SYSTEM ═══\n");
        
        // Build the chain: Bot → Junior → Senior → Manager
        SupportHandler bot = new BotSupport();
        SupportHandler junior = new JuniorAgent();
        SupportHandler senior = new SeniorAgent();
        SupportHandler manager = new Manager();
        
        bot.setNext(junior).setNext(senior).setNext(manager);
        
        // Test different tickets
        SupportTicket[] tickets = {
            new SupportTicket("How to reset password?", SupportTicket.Priority.LOW),
            new SupportTicket("Payment not working", SupportTicket.Priority.MEDIUM),
            new SupportTicket("Data breach detected", SupportTicket.Priority.HIGH),
            new SupportTicket("System down for all users!", SupportTicket.Priority.CRITICAL)
        };
        
        // Process each ticket through the chain
        for (SupportTicket ticket : tickets) {
            System.out.println("📩 New Ticket: \"" + ticket.getIssue() + 
                "\" [" + ticket.getPriority() + "]");
            bot.handle(ticket);  // Always start from first handler
            System.out.println();
        }
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * THE CHAIN FLOW:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     LOW ticket:
 *     ───────────
 *         📩 → 🤖 Bot (handles!) ✅
 *     
 *     MEDIUM ticket:
 *     ──────────────
 *         📩 → 🤖 Bot (can't) → 👨‍💻 Junior (handles!) ✅
 *     
 *     HIGH ticket:
 *     ────────────
 *         📩 → 🤖 Bot (can't) → 👨‍💻 Junior (can't) → 👩‍💼 Senior (handles!) ✅
 *     
 *     CRITICAL ticket:
 *     ────────────────
 *         📩 → 🤖 Bot (can't) → 👨‍💻 Junior (can't) → 👩‍💼 Senior (can't) → 🧑‍💼 Manager ✅
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * WITHOUT CHAIN (BAD - Big if-else):
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     void handleTicket(Ticket t) {
 *         if (t.priority == LOW) {
 *             bot.handle(t);
 *         } else if (t.priority == MEDIUM) {
 *             junior.handle(t);
 *         } else if (t.priority == HIGH) {
 *             senior.handle(t);
 *         } else {
 *             manager.handle(t);
 *         }
 *         // Adding new level = modify this code!
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * WITH CHAIN (GOOD):
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     - Each handler decides if it can handle
 *     - Easy to add/remove handlers
 *     - Order can be changed easily
 *     - Handlers are independent
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * REAL-WORLD EXAMPLES:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     1. CUSTOMER SUPPORT
 *        - Bot → Agent → Supervisor → Manager
 *     
 *     2. EXPENSE APPROVAL
 *        - <$100: Team Lead approves
 *        - <$1000: Manager approves
 *        - <$10000: Director approves
 *        - >$10000: CEO approves
 *     
 *     3. LOGGING FRAMEWORK
 *        - DEBUG → INFO → WARN → ERROR
 *        - Each level decides to log or pass
 *     
 *     4. SERVLET FILTERS (Java Web)
 *        - Authentication → Authorization → Logging → Actual Handler
 *     
 *     5. MIDDLEWARE (Express.js, Spring)
 *        - Request passes through chain of middleware
 *     
 *     6. EVENT BUBBLING (DOM)
 *        - Click event bubbles: Button → Div → Body → Document
 *     
 *     7. EXCEPTION HANDLING
 *        - try-catch chain: specific → general → finally
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * CHAIN vs COMMAND:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     CHAIN OF RESPONSIBILITY:
 *        - Request passed UNTIL someone handles it
 *        - "Who will handle this?"
 *     
 *     COMMAND:
 *        - Request wrapped as object
 *        - "What to do" (with undo/queue)
 */
