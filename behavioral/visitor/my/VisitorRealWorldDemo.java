/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * VISITOR PATTERN - REAL WORLD EXAMPLE
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * SCENARIO: E-Commerce Shopping Cart
 * 
 * You have different product types: Book, Electronics, Food
 * You want to perform different operations:
 *   - Calculate Tax (different tax for each product type)
 *   - Calculate Shipping Cost
 *   - Generate Invoice
 * 
 * Without Visitor: Add tax(), shipping(), invoice() to EVERY product class
 * With Visitor: Products stay clean, operations in separate visitor classes
 */

import java.util.ArrayList;
import java.util.List;

// ═══════════════════════════════════════════════════════════════════════════════
// STEP 1: Visitor Interface
// ═══════════════════════════════════════════════════════════════════════════════
interface ShoppingVisitor {
    double visit(Book book);
    double visit(Electronics electronics);
    double visit(Food food);
}

// ═══════════════════════════════════════════════════════════════════════════════
// STEP 2: Product Interface (Visitable)
// ═══════════════════════════════════════════════════════════════════════════════
interface Product {
    double accept(ShoppingVisitor visitor);
    String getName();
    double getPrice();
}

// ═══════════════════════════════════════════════════════════════════════════════
// STEP 3: Concrete Products
// ═══════════════════════════════════════════════════════════════════════════════
class Book implements Product {
    private String name;
    private double price;
    private String isbn;
    
    public Book(String name, double price, String isbn) {
        this.name = name;
        this.price = price;
        this.isbn = isbn;
    }
    
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getIsbn() { return isbn; }
    
    public double accept(ShoppingVisitor visitor) {
        return visitor.visit(this);
    }
}

class Electronics implements Product {
    private String name;
    private double price;
    private double weightKg;
    
    public Electronics(String name, double price, double weightKg) {
        this.name = name;
        this.price = price;
        this.weightKg = weightKg;
    }
    
    public String getName() { return name; }
    public double getPrice() { return price; }
    public double getWeightKg() { return weightKg; }
    
    public double accept(ShoppingVisitor visitor) {
        return visitor.visit(this);
    }
}

class Food implements Product {
    private String name;
    private double price;
    private boolean isPerishable;
    
    public Food(String name, double price, boolean isPerishable) {
        this.name = name;
        this.price = price;
        this.isPerishable = isPerishable;
    }
    
    public String getName() { return name; }
    public double getPrice() { return price; }
    public boolean isPerishable() { return isPerishable; }
    
    public double accept(ShoppingVisitor visitor) {
        return visitor.visit(this);
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// STEP 4: Concrete Visitors (Different Operations)
// ═══════════════════════════════════════════════════════════════════════════════

// TAX CALCULATOR - Different tax rates for different products
class TaxCalculator implements ShoppingVisitor {
    
    public double visit(Book book) {
        // Books have 0% tax (educational)
        return 0;
    }
    
    public double visit(Electronics electronics) {
        // Electronics have 18% GST
        return electronics.getPrice() * 0.18;
    }
    
    public double visit(Food food) {
        // Food has 5% tax
        return food.getPrice() * 0.05;
    }
}

// SHIPPING CALCULATOR - Different shipping costs
class ShippingCalculator implements ShoppingVisitor {
    
    public double visit(Book book) {
        // Flat ₹30 for books
        return 30;
    }
    
    public double visit(Electronics electronics) {
        // ₹50 per kg for electronics
        return electronics.getWeightKg() * 50;
    }
    
    public double visit(Food food) {
        // Perishable food needs express shipping
        return food.isPerishable() ? 100 : 40;
    }
}

// DISCOUNT CALCULATOR - Festival sale!
class DiscountCalculator implements ShoppingVisitor {
    
    public double visit(Book book) {
        // 10% off on books
        return book.getPrice() * 0.10;
    }
    
    public double visit(Electronics electronics) {
        // 5% off on electronics
        return electronics.getPrice() * 0.05;
    }
    
    public double visit(Food food) {
        // No discount on food
        return 0;
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// Shopping Cart
// ═══════════════════════════════════════════════════════════════════════════════
class ShoppingCart {
    private List<Product> products = new ArrayList<>();
    
    public void addProduct(Product product) {
        products.add(product);
    }
    
    public double calculateTotal(ShoppingVisitor visitor) {
        double total = 0;
        for (Product product : products) {
            total += product.accept(visitor);
        }
        return total;
    }
    
    public double getSubtotal() {
        return products.stream().mapToDouble(Product::getPrice).sum();
    }
    
    public List<Product> getProducts() {
        return products;
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// DEMO
// ═══════════════════════════════════════════════════════════════════════════════
public class VisitorRealWorldDemo {
    
    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════════");
        System.out.println("       SHOPPING CART - VISITOR PATTERN DEMO");
        System.out.println("═══════════════════════════════════════════════════════\n");
        
        // Create cart and add products
        ShoppingCart cart = new ShoppingCart();
        cart.addProduct(new Book("Clean Code", 500, "ISBN-123"));
        cart.addProduct(new Electronics("Laptop", 50000, 2.5));
        cart.addProduct(new Food("Cheese", 200, true));
        cart.addProduct(new Food("Rice", 300, false));
        
        // Display products
        System.out.println("PRODUCTS IN CART:");
        System.out.println("─────────────────────────────────────────────────────");
        for (Product p : cart.getProducts()) {
            System.out.printf("  %-20s ₹%.2f%n", p.getName(), p.getPrice());
        }
        
        // Calculate using different visitors
        double subtotal = cart.getSubtotal();
        double tax = cart.calculateTotal(new TaxCalculator());
        double shipping = cart.calculateTotal(new ShippingCalculator());
        double discount = cart.calculateTotal(new DiscountCalculator());
        
        System.out.println("─────────────────────────────────────────────────────");
        System.out.printf("  Subtotal:          ₹%.2f%n", subtotal);
        System.out.printf("  Tax:               ₹%.2f%n", tax);
        System.out.printf("  Shipping:          ₹%.2f%n", shipping);
        System.out.printf("  Discount:         -₹%.2f%n", discount);
        System.out.println("═════════════════════════════════════════════════════");
        System.out.printf("  TOTAL:             ₹%.2f%n", subtotal + tax + shipping - discount);
        System.out.println("═════════════════════════════════════════════════════");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * OTHER REAL-WORLD USES OF VISITOR PATTERN:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * 1. COMPILER/AST
 *    - Elements: Different AST nodes (IfStatement, WhileLoop, Assignment)
 *    - Visitors: TypeChecker, CodeGenerator, Optimizer
 * 
 * 2. FILE SYSTEM
 *    - Elements: File, Folder
 *    - Visitors: SizeCalculator, Compressor, VirusScanner
 * 
 * 3. DOCUMENT EXPORT
 *    - Elements: Paragraph, Image, Table
 *    - Visitors: PDFExporter, HTMLExporter, WordExporter
 * 
 * 4. INSURANCE
 *    - Elements: Car, Home, Health policy
 *    - Visitors: PremiumCalculator, ClaimProcessor, RiskAssessor
 * 
 * 5. GAME
 *    - Elements: Player, Enemy, PowerUp
 *    - Visitors: CollisionHandler, Renderer, AIController
 */
