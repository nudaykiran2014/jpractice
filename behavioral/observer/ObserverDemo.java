package behavioral.observer;

public class ObserverDemo {
    public static void main(String[] args) {
        System.out.println("=== Observer Pattern Demo ===\n");
        
        NewsAgency newsAgency = new NewsAgency();
        
        NewsChannel channel1 = new NewsChannel("CNN");
        NewsChannel channel2 = new NewsChannel("BBC");
        NewsChannel channel3 = new NewsChannel("Fox News");
        
        newsAgency.attach(channel1);
        newsAgency.attach(channel2);
        newsAgency.attach(channel3);
        
        System.out.println("Broadcasting first news:");
        newsAgency.setNews("Breaking: New design pattern discovered!");
        
        System.out.println("\nRemoving Fox News from subscribers:");
        newsAgency.detach(channel3);
        
        System.out.println("\nBroadcasting second news:");
        newsAgency.setNews("Update: Observer pattern is trending!");
    }
}
