/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * COMPOSITE PATTERN - Explained Like You're a Kid! 🧒
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * STORY TIME! 📖
 * ──────────────
 * Imagine you have a BOX OF TOYS 📦
 * 
 * Inside the box you can put:
 *   - Individual toys 🧸 (leaf)
 *   - OR another box 📦 with more toys inside! (composite)
 *   
 *     📦 Big Box
 *      ├── 🧸 Teddy Bear
 *      ├── 🚗 Toy Car
 *      └── 📦 Small Box
 *           ├── 🎮 Game
 *           └── 📦 Tiny Box
 *                └── 🔮 Marble
 * 
 * THE MAGIC:
 *   - You can ask "What's your price?" to BOTH toy and box
 *   - Box calculates price of ALL items inside!
 *   - Same interface for leaf (toy) and composite (box)!
 * 
 * THE PATTERN:
 * ─────────────
 *     Component (interface)
 *          │
 *     ┌────┴────┐
 *     │         │
 *    Leaf    Composite
 *   (toy)     (box)
 *              └── contains Components
 */

import java.util.ArrayList;
import java.util.List;

// ═══════════════════════════════════════════════════════════════════════════════
// COMPONENT - Common interface for both leaf and composite
// ═══════════════════════════════════════════════════════════════════════════════
interface Employee {
    String getName();
    double getSalary();
    void showDetails(String indent);
}

// ═══════════════════════════════════════════════════════════════════════════════
// LEAF - Individual employee (no subordinates)
// ═══════════════════════════════════════════════════════════════════════════════
class Developer implements Employee {
    private String name;
    private double salary;
    
    public Developer(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }
    
    public String getName() { return name; }
    public double getSalary() { return salary; }
    
    public void showDetails(String indent) {
        System.out.println(indent + "👨‍💻 " + name + " (Developer) - ₹" + salary);
    }
}

class Designer implements Employee {
    private String name;
    private double salary;
    
    public Designer(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }
    
    public String getName() { return name; }
    public double getSalary() { return salary; }
    
    public void showDetails(String indent) {
        System.out.println(indent + "🎨 " + name + " (Designer) - ₹" + salary);
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// COMPOSITE - Manager with subordinates
// ═══════════════════════════════════════════════════════════════════════════════
class Manager implements Employee {
    private String name;
    private double salary;
    private List<Employee> subordinates = new ArrayList<>();
    
    public Manager(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }
    
    public void addEmployee(Employee e) {
        subordinates.add(e);
    }
    
    public void removeEmployee(Employee e) {
        subordinates.remove(e);
    }
    
    public String getName() { return name; }
    
    // Salary includes all subordinates!
    public double getSalary() {
        double total = salary;
        for (Employee e : subordinates) {
            total += e.getSalary();
        }
        return total;
    }
    
    public void showDetails(String indent) {
        System.out.println(indent + "👔 " + name + " (Manager) - ₹" + salary + 
            " [Team cost: ₹" + getSalary() + "]");
        for (Employee e : subordinates) {
            e.showDetails(indent + "  ");
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// REAL-WORLD EXAMPLE: Menu System
// ═══════════════════════════════════════════════════════════════════════════════

interface MenuComponent {
    String getName();
    double getPrice();
    void display(String indent);
}

// Leaf - Individual menu item
class MenuItem implements MenuComponent {
    private String name;
    private double price;
    
    public MenuItem(String name, double price) {
        this.name = name;
        this.price = price;
    }
    
    public String getName() { return name; }
    public double getPrice() { return price; }
    
    public void display(String indent) {
        System.out.println(indent + "🍽️ " + name + " - ₹" + price);
    }
}

// Composite - Menu category (contains items or other menus)
class Menu implements MenuComponent {
    private String name;
    private List<MenuComponent> items = new ArrayList<>();
    
    public Menu(String name) {
        this.name = name;
    }
    
    public void add(MenuComponent item) {
        items.add(item);
    }
    
    public String getName() { return name; }
    
    public double getPrice() {
        return items.stream().mapToDouble(MenuComponent::getPrice).sum();
    }
    
    public void display(String indent) {
        System.out.println(indent + "📋 " + name + " [Total: ₹" + getPrice() + "]");
        for (MenuComponent item : items) {
            item.display(indent + "  ");
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// DEMO
// ═══════════════════════════════════════════════════════════════════════════════
public class CompositeMyDemo {
    
    public static void main(String[] args) {
        System.out.println("═══ COMPOSITE PATTERN - ORGANIZATION ═══\n");
        
        // Create leaf employees
        Developer dev1 = new Developer("John", 80000);
        Developer dev2 = new Developer("Jane", 85000);
        Developer dev3 = new Developer("Bob", 75000);
        Designer des1 = new Designer("Alice", 70000);
        
        // Create composite managers
        Manager techLead = new Manager("Mike", 120000);
        techLead.addEmployee(dev1);
        techLead.addEmployee(dev2);
        
        Manager designLead = new Manager("Sarah", 110000);
        designLead.addEmployee(des1);
        
        // CTO manages both leads
        Manager cto = new Manager("David", 200000);
        cto.addEmployee(techLead);
        cto.addEmployee(designLead);
        cto.addEmployee(dev3);  // Direct report too!
        
        // Show organization
        System.out.println("Organization Structure:");
        cto.showDetails("  ");
        
        System.out.println("\n\n═══ COMPOSITE PATTERN - RESTAURANT MENU ═══\n");
        
        // Create main menu
        Menu mainMenu = new Menu("Main Menu");
        
        // Breakfast submenu
        Menu breakfast = new Menu("Breakfast");
        breakfast.add(new MenuItem("Idli", 40));
        breakfast.add(new MenuItem("Dosa", 60));
        breakfast.add(new MenuItem("Poha", 35));
        
        // Lunch submenu
        Menu lunch = new Menu("Lunch");
        lunch.add(new MenuItem("Thali", 120));
        lunch.add(new MenuItem("Biryani", 150));
        
        // Drinks submenu inside lunch
        Menu drinks = new Menu("Beverages");
        drinks.add(new MenuItem("Lassi", 40));
        drinks.add(new MenuItem("Chai", 20));
        lunch.add(drinks);  // Nested menu!
        
        // Add to main menu
        mainMenu.add(breakfast);
        mainMenu.add(lunch);
        mainMenu.add(new MenuItem("Ice Cream", 50));  // Direct item
        
        // Display entire menu
        System.out.println("Restaurant Menu:");
        mainMenu.display("  ");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * WITHOUT COMPOSITE (BAD):
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     // Must handle leaf and composite differently!
 *     if (item instanceof MenuItem) {
 *         MenuItem mi = (MenuItem) item;
 *         System.out.println(mi.getName());
 *     } else if (item instanceof Menu) {
 *         Menu m = (Menu) item;
 *         for (Object child : m.getItems()) {
 *             // Recursively check type again!
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * WITH COMPOSITE (GOOD):
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     // Same interface for both!
 *     item.display();  // Works for MenuItem AND Menu!
 *     item.getPrice(); // Works for both!
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * REAL-WORLD EXAMPLES:
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 *     1. FILE SYSTEM
 *        - File (leaf) and Folder (composite)
 *     
 *     2. ORGANIZATION CHART
 *        - Employee (leaf) and Manager (composite)
 *     
 *     3. GUI WIDGETS
 *        - Button (leaf) and Panel/Container (composite)
 *     
 *     4. HTML DOM
 *        - Text node (leaf) and Element (composite)
 *     
 *     5. SHOPPING CART
 *        - Product (leaf) and Bundle/Box (composite)
 */
