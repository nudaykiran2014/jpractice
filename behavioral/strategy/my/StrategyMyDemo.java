/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * STRATEGY PATTERN
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * WHAT: Define a family of algorithms, put each in a separate class,
 *       and make them interchangeable at runtime.
 * 
 * WHEN TO USE:
 * - You have multiple ways to do the same thing (e.g., different payment methods)
 * - You want to avoid lots of if-else or switch statements
 * - You want to change behavior at runtime
 * 
 * STRUCTURE:
 *     ┌─────────────────┐         ┌─────────────────┐
 *     │     Context     │────────>│    Strategy     │  (interface)
 *     │  (ShoppingCart) │         │    (Payment)    │
 *     └─────────────────┘         └────────┬────────┘
 *                                          │
 *                    ┌─────────────────────┼─────────────────────┐
 *                    │                     │                     │
 *           ┌────────▼────────┐  ┌────────▼────────┐  ┌────────▼────────┐
 *           │  CreditCard     │  │    PayPal       │  │     UPI         │
 *           │  Strategy       │  │    Strategy     │  │   Strategy      │
 *           └─────────────────┘  └─────────────────┘  └─────────────────┘
 */

// ═══════════════════════════════════════════════════════════════════════════════
// STEP 1: Define the Strategy Interface
// ═══════════════════════════════════════════════════════════════════════════════
interface PaymentStrategy {
    void pay(int amount);
}

// ═══════════════════════════════════════════════════════════════════════════════
// STEP 2: Implement Concrete Strategies
// ═══════════════════════════════════════════════════════════════════════════════
class CreditCardPayment implements PaymentStrategy {
    private String cardNumber;
    
    public CreditCardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    
    @Override
    public void pay(int amount) {
        System.out.println("Paid ₹" + amount + " using Credit Card: " + cardNumber);
    }
}

class PayPalPayment implements PaymentStrategy {
    private String email;
    
    public PayPalPayment(String email) {
        this.email = email;
    }
    
    @Override
    public void pay(int amount) {
        System.out.println("Paid ₹" + amount + " using PayPal: " + email);
    }
}

class UPIPayment implements PaymentStrategy {
    private String upiId;
    
    public UPIPayment(String upiId) {
        this.upiId = upiId;
    }
    
    @Override
    public void pay(int amount) {
        System.out.println("Paid ₹" + amount + " using UPI: " + upiId);
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// STEP 3: Context Class (uses the strategy)
// ═══════════════════════════════════════════════════════════════════════════════
class ShoppingCart {
    private PaymentStrategy paymentStrategy;  // HAS-A strategy
    
    // Set strategy at runtime!
    public void setPaymentStrategy(PaymentStrategy strategy) {
        this.paymentStrategy = strategy;
    }
    
    public void checkout(int amount) {
        if (paymentStrategy == null) {
            System.out.println("Please select a payment method!");
            return;
        }
        paymentStrategy.pay(amount);  // Delegate to strategy
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// DEMO
// ═══════════════════════════════════════════════════════════════════════════════
class StrategyMyDemo {
    
    public static void main(String[] args) {
        System.out.println("═══ STRATEGY PATTERN DEMO ═══\n");
        
        ShoppingCart cart = new ShoppingCart();
        
        // Pay with Credit Card
        cart.setPaymentStrategy(new CreditCardPayment("1234-5678-9012-3456"));
        cart.checkout(1000);
        
        // Change strategy at runtime - Pay with PayPal
        cart.setPaymentStrategy(new PayPalPayment("user@email.com"));
        cart.checkout(2000);
        
        // Change strategy again - Pay with UPI
        cart.setPaymentStrategy(new UPIPayment("user@upi"));
        cart.checkout(500);
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * WITHOUT STRATEGY (BAD - lots of if-else):
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     void pay(String method, int amount) {
 *         if (method.equals("creditcard")) {
 *             // credit card logic
 *         } else if (method.equals("paypal")) {
 *             // paypal logic
 *         } else if (method.equals("upi")) {
 *             // upi logic
 *         }
 *         // Adding new method = modify this code! ❌
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * WITH STRATEGY (GOOD - Open/Closed Principle):
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     - Adding new payment method = Just create new class ✅
 *     - No modification to existing code
 *     - Easy to test each strategy separately
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * REAL-WORLD EXAMPLES:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     - Sorting algorithms (QuickSort, MergeSort, BubbleSort)
 *     - Compression algorithms (ZIP, RAR, GZIP)
 *     - Validation strategies (EmailValidator, PhoneValidator)
 *     - Discount strategies (FlatDiscount, PercentageDiscount)
 *     - Authentication strategies (OAuth, JWT, Basic)
 */