package behavioral.strategy;

public class PayPalPayment implements PaymentStrategy {
    private String email;
    
    public PayPalPayment(String email) {
        this.email = email;
    }
    
    @Override
    public void pay(int amount) {
        System.out.println("Paid $" + amount + " using PayPal");
        System.out.println("PayPal Email: " + email);
    }
}
