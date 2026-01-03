package structural.composite;

public class CompositeDemo {
    public static void main(String[] args) {
        System.out.println("=== Composite Pattern Demo ===\n");
        
        Employee CEO = new Employee("John", "CEO", 30000);
        
        Employee headSales = new Employee("Robert", "Head Sales", 20000);
        Employee headMarketing = new Employee("Michel", "Head Marketing", 20000);
        
        Employee salesExecutive1 = new Employee("Richard", "Sales Executive", 10000);
        Employee salesExecutive2 = new Employee("Rob", "Sales Executive", 10000);
        
        Employee marketingExecutive1 = new Employee("Laura", "Marketing Executive", 10000);
        Employee marketingExecutive2 = new Employee("Bob", "Marketing Executive", 10000);
        
        CEO.add(headSales);
        CEO.add(headMarketing);
        
        headSales.add(salesExecutive1);
        headSales.add(salesExecutive2);
        
        headMarketing.add(marketingExecutive1);
        headMarketing.add(marketingExecutive2);
        
        System.out.println("Organization Structure:");
        CEO.printStructure("");
    }
}
