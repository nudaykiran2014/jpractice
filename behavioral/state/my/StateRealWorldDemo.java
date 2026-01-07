/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * STATE PATTERN - REAL WORLD EXAMPLE: Order Status
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * SCENARIO: E-commerce Order
 * 
 * Order goes through states:
 *   PENDING → CONFIRMED → SHIPPED → DELIVERED
 *                ↓
 *            CANCELLED
 * 
 * Each state has different allowed actions:
 *   - PENDING: can confirm or cancel
 *   - CONFIRMED: can ship or cancel
 *   - SHIPPED: can deliver (cannot cancel!)
 *   - DELIVERED: nothing more can be done
 */

// ═══════════════════════════════════════════════════════════════════════════════
// State Interface
// ═══════════════════════════════════════════════════════════════════════════════
interface OrderState {
    void confirm(Order order);
    void ship(Order order);
    void deliver(Order order);
    void cancel(Order order);
    String getStatus();
}

// ═══════════════════════════════════════════════════════════════════════════════
// Concrete States
// ═══════════════════════════════════════════════════════════════════════════════

class PendingState implements OrderState {
    
    public String getStatus() { return "📋 PENDING"; }
    
    public void confirm(Order order) {
        System.out.println("  ✅ Order confirmed! Processing payment...");
        order.setState(new ConfirmedState());
    }
    
    public void ship(Order order) {
        System.out.println("  ❌ Cannot ship! Order not confirmed yet.");
    }
    
    public void deliver(Order order) {
        System.out.println("  ❌ Cannot deliver! Order not shipped yet.");
    }
    
    public void cancel(Order order) {
        System.out.println("  🚫 Order cancelled. Full refund initiated.");
        order.setState(new CancelledState());
    }
}

class ConfirmedState implements OrderState {
    
    public String getStatus() { return "✅ CONFIRMED"; }
    
    public void confirm(Order order) {
        System.out.println("  ⚠️ Order already confirmed.");
    }
    
    public void ship(Order order) {
        System.out.println("  📦 Order shipped! Tracking ID: TRK" + System.currentTimeMillis() % 10000);
        order.setState(new ShippedState());
    }
    
    public void deliver(Order order) {
        System.out.println("  ❌ Cannot deliver! Order not shipped yet.");
    }
    
    public void cancel(Order order) {
        System.out.println("  🚫 Order cancelled. Refund in 3-5 business days.");
        order.setState(new CancelledState());
    }
}

class ShippedState implements OrderState {
    
    public String getStatus() { return "📦 SHIPPED"; }
    
    public void confirm(Order order) {
        System.out.println("  ⚠️ Order already confirmed and shipped.");
    }
    
    public void ship(Order order) {
        System.out.println("  ⚠️ Order already shipped.");
    }
    
    public void deliver(Order order) {
        System.out.println("  🎉 Order delivered! Thank you for shopping!");
        order.setState(new DeliveredState());
    }
    
    public void cancel(Order order) {
        System.out.println("  ❌ Cannot cancel! Order already shipped. Please return after delivery.");
    }
}

class DeliveredState implements OrderState {
    
    public String getStatus() { return "🎉 DELIVERED"; }
    
    public void confirm(Order order) {
        System.out.println("  ⚠️ Order already delivered.");
    }
    
    public void ship(Order order) {
        System.out.println("  ⚠️ Order already delivered.");
    }
    
    public void deliver(Order order) {
        System.out.println("  ⚠️ Order already delivered.");
    }
    
    public void cancel(Order order) {
        System.out.println("  ❌ Cannot cancel delivered order. Please initiate return.");
    }
}

class CancelledState implements OrderState {
    
    public String getStatus() { return "🚫 CANCELLED"; }
    
    public void confirm(Order order) {
        System.out.println("  ❌ Cannot confirm. Order is cancelled.");
    }
    
    public void ship(Order order) {
        System.out.println("  ❌ Cannot ship. Order is cancelled.");
    }
    
    public void deliver(Order order) {
        System.out.println("  ❌ Cannot deliver. Order is cancelled.");
    }
    
    public void cancel(Order order) {
        System.out.println("  ⚠️ Order already cancelled.");
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// Context: Order
// ═══════════════════════════════════════════════════════════════════════════════
class Order {
    private String orderId;
    private OrderState state;
    
    public Order(String orderId) {
        this.orderId = orderId;
        this.state = new PendingState();
    }
    
    public void setState(OrderState state) {
        this.state = state;
    }
    
    public void confirm() { state.confirm(this); }
    public void ship() { state.ship(this); }
    public void deliver() { state.deliver(this); }
    public void cancel() { state.cancel(this); }
    
    public void printStatus() {
        System.out.println("Order " + orderId + ": " + state.getStatus());
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// DEMO
// ═══════════════════════════════════════════════════════════════════════════════
public class StateRealWorldDemo {
    
    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("      STATE PATTERN - ORDER STATUS EXAMPLE");
        System.out.println("═══════════════════════════════════════════════════════\n");
        
        // Order 1: Happy path (full journey)
        System.out.println("--- ORDER #1: Happy Path ---");
        Order order1 = new Order("ORD-001");
        order1.printStatus();
        order1.confirm();
        order1.printStatus();
        order1.ship();
        order1.printStatus();
        order1.deliver();
        order1.printStatus();
        
        System.out.println("\n--- ORDER #2: Cancel after confirm ---");
        Order order2 = new Order("ORD-002");
        order2.printStatus();
        order2.confirm();
        order2.printStatus();
        order2.cancel();
        order2.printStatus();
        
        System.out.println("\n--- ORDER #3: Try invalid actions ---");
        Order order3 = new Order("ORD-003");
        order3.printStatus();
        order3.ship();      // Can't ship without confirming!
        order3.deliver();   // Can't deliver without shipping!
        order3.confirm();
        order3.printStatus();
        order3.ship();
        order3.printStatus();
        order3.cancel();    // Can't cancel after shipping!
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * STATE DIAGRAM:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     ┌─────────┐  confirm   ┌───────────┐  ship   ┌─────────┐  deliver  ┌───────────┐
 *     │ PENDING │──────────>│ CONFIRMED │───────>│ SHIPPED │─────────>│ DELIVERED │
 *     └────┬────┘            └─────┬─────┘        └─────────┘          └───────────┘
 *          │                       │                   │
 *          │ cancel                │ cancel            │ (cannot cancel!)
 *          │                       │                   │
 *          ▼                       ▼                   │
 *     ┌───────────────────────────────┐               │
 *     │          CANCELLED            │<──────────────┘ (must return instead)
 *     └───────────────────────────────┘
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * WHY STATE PATTERN HERE?
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Without State Pattern:
 *     void ship() {
 *         if (status == PENDING) throw new Exception("Not confirmed");
 *         if (status == SHIPPED) throw new Exception("Already shipped");
 *         if (status == DELIVERED) throw new Exception("Already delivered");
 *         if (status == CANCELLED) throw new Exception("Order cancelled");
 *         // Finally do the actual work
 *         status = SHIPPED;
 *     }
 *     // Same ugly if-else in EVERY method!
 * 
 * With State Pattern:
 *     - Each state knows what IT can do
 *     - Clean, no if-else chains
 *     - Easy to add new states
 */
