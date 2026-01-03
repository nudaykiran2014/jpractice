package behavioral.iterator;

public class IteratorDemo {
    public static void main(String[] args) {
        System.out.println("=== Iterator Pattern Demo ===\n");
        
        NameRepository nameRepository = new NameRepository();
        
        System.out.println("Iterating through names:");
        for (Iterator<String> iter = nameRepository.getIterator(); iter.hasNext();) {
            String name = iter.next();
            System.out.println("Name: " + name);
        }
    }
}
