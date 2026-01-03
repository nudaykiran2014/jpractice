package behavioral.strategy;

public class BitcoinPayment implements PaymentStrategy {
    private String walletAddress;
    
    public BitcoinPayment(String walletAddress) {
        this.walletAddress = walletAddress;
    }
    
    @Override
    public void pay(int amount) {
        System.out.println("Paid $" + amount + " using Bitcoin");
        System.out.println("Wallet Address: " + walletAddress);
    }
}
