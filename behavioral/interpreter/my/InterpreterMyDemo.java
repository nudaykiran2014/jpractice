/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * INTERPRETER PATTERN - Explained Like You're a Kid! 🧒
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * STORY TIME! 📖
 * ──────────────
 * Imagine you're a ROBOT 🤖 that only understands simple commands:
 * 
 *   "MOVE FORWARD"
 *   "TURN LEFT"
 *   "TURN RIGHT"
 *   "MOVE FORWARD AND TURN LEFT"
 *   "MOVE FORWARD OR TURN RIGHT"
 * 
 * You need to INTERPRET (understand) these commands and DO them!
 * 
 * THE PATTERN:
 * ─────────────
 *   - Define a GRAMMAR (rules for the language)
 *   - Each rule becomes a CLASS
 *   - Combine simple rules to make complex ones
 *   
 *       "5 + 3 - 2"
 *           ↓
 *       ┌─────────┐
 *       │   -     │        (Subtract)
 *       └────┬────┘
 *          ┌─┴─┐
 *       ┌──┴──┐  2
 *       │  +  │            (Add)
 *       └──┬──┘
 *        ┌─┴─┐
 *        5   3
 * 
 * REAL USE: SQL parsers, calculators, regular expressions, rules engines
 */

// ═══════════════════════════════════════════════════════════════════════════════
// EXAMPLE 1: Simple Math Calculator 🧮
// ═══════════════════════════════════════════════════════════════════════════════

// STEP 1: Expression Interface (the grammar)
interface MathExpression {
    int interpret();
}

// STEP 2: Terminal Expression (numbers - the leaves)
class NumberExpression implements MathExpression {
    private int number;
    
    public NumberExpression(int number) {
        this.number = number;
    }
    
    public int interpret() {
        return number;  // Just return the number
    }
}

// STEP 3: Non-Terminal Expressions (operations - combine others)
class AddExpression implements MathExpression {
    private MathExpression left;
    private MathExpression right;
    
    public AddExpression(MathExpression left, MathExpression right) {
        this.left = left;
        this.right = right;
    }
    
    public int interpret() {
        return left.interpret() + right.interpret();
    }
}

class SubtractExpression implements MathExpression {
    private MathExpression left;
    private MathExpression right;
    
    public SubtractExpression(MathExpression left, MathExpression right) {
        this.left = left;
        this.right = right;
    }
    
    public int interpret() {
        return left.interpret() - right.interpret();
    }
}

class MultiplyExpression implements MathExpression {
    private MathExpression left;
    private MathExpression right;
    
    public MultiplyExpression(MathExpression left, MathExpression right) {
        this.left = left;
        this.right = right;
    }
    
    public int interpret() {
        return left.interpret() * right.interpret();
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// EXAMPLE 2: Rule Engine (Business Rules) 📋
// ═══════════════════════════════════════════════════════════════════════════════

// Context - the data we're checking rules against
class Customer {
    String name;
    int age;
    boolean isPremium;
    double totalPurchases;
    
    public Customer(String name, int age, boolean isPremium, double totalPurchases) {
        this.name = name;
        this.age = age;
        this.isPremium = isPremium;
        this.totalPurchases = totalPurchases;
    }
}

// Rule Expression Interface
interface RuleExpression {
    boolean interpret(Customer customer);
}

// Terminal Rules (basic conditions)
class AgeRule implements RuleExpression {
    private int minAge;
    
    public AgeRule(int minAge) {
        this.minAge = minAge;
    }
    
    public boolean interpret(Customer customer) {
        return customer.age >= minAge;
    }
    
    public String toString() { return "Age >= " + minAge; }
}

class PremiumRule implements RuleExpression {
    public boolean interpret(Customer customer) {
        return customer.isPremium;
    }
    
    public String toString() { return "Is Premium"; }
}

class PurchaseRule implements RuleExpression {
    private double minPurchase;
    
    public PurchaseRule(double minPurchase) {
        this.minPurchase = minPurchase;
    }
    
    public boolean interpret(Customer customer) {
        return customer.totalPurchases >= minPurchase;
    }
    
    public String toString() { return "Purchases >= ₹" + minPurchase; }
}

// Non-Terminal Rules (combine rules)
class AndRule implements RuleExpression {
    private RuleExpression rule1;
    private RuleExpression rule2;
    
    public AndRule(RuleExpression rule1, RuleExpression rule2) {
        this.rule1 = rule1;
        this.rule2 = rule2;
    }
    
    public boolean interpret(Customer customer) {
        return rule1.interpret(customer) && rule2.interpret(customer);
    }
    
    public String toString() { return "(" + rule1 + " AND " + rule2 + ")"; }
}

class OrRule implements RuleExpression {
    private RuleExpression rule1;
    private RuleExpression rule2;
    
    public OrRule(RuleExpression rule1, RuleExpression rule2) {
        this.rule1 = rule1;
        this.rule2 = rule2;
    }
    
    public boolean interpret(Customer customer) {
        return rule1.interpret(customer) || rule2.interpret(customer);
    }
    
    public String toString() { return "(" + rule1 + " OR " + rule2 + ")"; }
}

// ═══════════════════════════════════════════════════════════════════════════════
// DEMO
// ═══════════════════════════════════════════════════════════════════════════════
public class InterpreterMyDemo {
    
    public static void main(String[] args) {
        System.out.println("═══ INTERPRETER PATTERN ═══\n");
        
        // ─────────────────────────────────────────────────────────────────────
        // EXAMPLE 1: Math Calculator
        // ─────────────────────────────────────────────────────────────────────
        System.out.println("🧮 MATH CALCULATOR:");
        System.out.println("───────────────────────────────────────");
        
        // Build expression: (5 + 3) * 2 = 16
        MathExpression five = new NumberExpression(5);
        MathExpression three = new NumberExpression(3);
        MathExpression two = new NumberExpression(2);
        
        MathExpression sum = new AddExpression(five, three);      // 5 + 3 = 8
        MathExpression result = new MultiplyExpression(sum, two); // 8 * 2 = 16
        
        System.out.println("  Expression: (5 + 3) * 2");
        System.out.println("  Result: " + result.interpret());
        
        // Build expression: 10 - 3 - 2 = 5
        MathExpression ten = new NumberExpression(10);
        MathExpression expr2 = new SubtractExpression(
            new SubtractExpression(ten, three), two
        );
        System.out.println("\n  Expression: 10 - 3 - 2");
        System.out.println("  Result: " + expr2.interpret());
        
        // ─────────────────────────────────────────────────────────────────────
        // EXAMPLE 2: Business Rules Engine
        // ─────────────────────────────────────────────────────────────────────
        System.out.println("\n\n📋 BUSINESS RULES ENGINE:");
        System.out.println("───────────────────────────────────────");
        
        // Create customers
        Customer john = new Customer("John", 25, true, 15000);
        Customer jane = new Customer("Jane", 17, false, 5000);
        Customer bob = new Customer("Bob", 30, false, 25000);
        
        // Define discount rule:
        // "Premium member" OR ("Age >= 18" AND "Purchases >= 10000")
        RuleExpression discountRule = new OrRule(
            new PremiumRule(),
            new AndRule(
                new AgeRule(18),
                new PurchaseRule(10000)
            )
        );
        
        System.out.println("  Rule: " + discountRule);
        System.out.println();
        
        // Check each customer
        checkDiscount(john, discountRule);
        checkDiscount(jane, discountRule);
        checkDiscount(bob, discountRule);
    }
    
    static void checkDiscount(Customer c, RuleExpression rule) {
        boolean eligible = rule.interpret(c);
        String status = eligible ? "✅ ELIGIBLE" : "❌ NOT ELIGIBLE";
        System.out.println("  " + c.name + " (Age:" + c.age + 
            ", Premium:" + c.isPremium + ", Purchases:₹" + c.totalPurchases + ")");
        System.out.println("    → " + status + " for discount\n");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * THE GRAMMAR TREE:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     Expression "(5 + 3) * 2" becomes:
 *     
 *              ┌─────────┐
 *              │    *    │  MultiplyExpression
 *              └────┬────┘
 *                 ┌─┴─┐
 *           ┌─────┴─┐  │
 *           │   +   │  2   NumberExpression
 *           └───┬───┘
 *             ┌─┴─┐
 *             5   3        NumberExpression
 *     
 *     interpret() walks the tree bottom-up:
 *       5 → 5
 *       3 → 3
 *       + → 5 + 3 = 8
 *       2 → 2
 *       * → 8 * 2 = 16
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * REAL-WORLD EXAMPLES:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     1. SQL PARSER
 *        - "SELECT * FROM users WHERE age > 18"
 *        - Each clause (SELECT, FROM, WHERE) is interpreted
 *     
 *     2. REGULAR EXPRESSIONS
 *        - Pattern "[a-z]+@[a-z]+\\.com"
 *        - Each part is an expression that matches text
 *     
 *     3. CALCULATOR APP
 *        - "2 + 3 * 4" → builds expression tree → evaluates
 *     
 *     4. RULE ENGINES (Drools)
 *        - Business rules like "IF premium AND age > 25 THEN discount"
 *     
 *     5. PROGRAMMING LANGUAGE COMPILERS
 *        - Parse code into AST (Abstract Syntax Tree)
 *        - Interpret/compile each node
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * WHEN TO USE:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     ✅ USE when:
 *        - You have a simple grammar/language to parse
 *        - Rules can be combined (AND, OR, NOT)
 *        - Grammar doesn't change often
 *     
 *     ❌ AVOID when:
 *        - Grammar is complex (use parser generators instead)
 *        - Performance is critical (tree walking is slow)
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * INTERPRETER vs VISITOR:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     INTERPRETER:
 *        - Each class interprets ITSELF
 *        - interpret() is inside the expression class
 *     
 *     VISITOR:
 *        - External visitor interprets classes
 *        - visit() is in separate visitor class
 */
