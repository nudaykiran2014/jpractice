package structural.composite;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    private String name;
    private String position;
    private double salary;
    private List<Employee> subordinates;
    
    public Employee(String name, String position, double salary) {
        this.name = name;
        this.position = position;
        this.salary = salary;
        this.subordinates = new ArrayList<>();
    }
    
    public void add(Employee employee) {
        subordinates.add(employee);
    }
    
    public void remove(Employee employee) {
        subordinates.remove(employee);
    }
    
    public List<Employee> getSubordinates() {
        return subordinates;
    }
    
    public void printStructure(String indent) {
        System.out.println(indent + name + " (" + position + ") - $" + salary);
        for (Employee subordinate : subordinates) {
            subordinate.printStructure(indent + "  ");
        }
    }
    
    @Override
    public String toString() {
        return "Employee: " + name + ", " + position + ", $" + salary;
    }
}
