package behavioral.strategy;

public class StrategyDemo {
    public static void main(String[] args) {
        System.out.println("=== Strategy Pattern Demo ===\n");
        
        ShoppingCart cart = new ShoppingCart();
        cart.addItem(new Item("Book", 30));
        cart.addItem(new Item("Pen", 5));
        cart.addItem(new Item("Laptop", 1000));
        
        System.out.println("Total amount: $" + cart.calculateTotal());
        System.out.println();
        
        System.out.println("Paying with Credit Card:");
        cart.pay(new CreditCardPayment("1234-5678-9012-3456", "John Doe"));
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        ShoppingCart cart2 = new ShoppingCart();
        cart2.addItem(new Item("Mouse", 25));
        cart2.addItem(new Item("Keyboard", 75));
        
        System.out.println("Total amount: $" + cart2.calculateTotal());
        System.out.println();
        
        System.out.println("Paying with PayPal:");
        cart2.pay(new PayPalPayment("john.doe@example.com"));
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        ShoppingCart cart3 = new ShoppingCart();
        cart3.addItem(new Item("Monitor", 300));
        
        System.out.println("Total amount: $" + cart3.calculateTotal());
        System.out.println();
        
        System.out.println("Paying with Bitcoin:");
        cart3.pay(new BitcoinPayment("1A1zP1eP5QGefi2DMPTfTL5SLmv7DivfNa"));
    }
}
