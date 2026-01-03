package spring_learning.lesson01_pojo;

/**
 * LESSON 1: What is a POJO?
 * 
 * POJO = Plain Old Java Object
 * 
 * Think of it like a simple box that holds your stuff:
 * - It has fields (the stuff inside the box)
 * - It has getters (ways to look inside the box)
 * - It has setters (ways to put stuff in the box)
 * - NO special framework magic required!
 * 
 * WHY IS THIS IMPORTANT FOR SPRING?
 * Spring loves POJOs! Before Spring, Java enterprise apps required
 * complex classes that extended special framework classes.
 * Spring said: "Just use simple POJOs, we'll handle the rest!"
 */
public class Person {
    
    // Fields - the data this object holds
    private String name;
    private int age;
    private String email;
    
    // Default constructor - required for many frameworks
    public Person() {
    }
    
    // Parameterized constructor - convenient way to create objects
    public Person(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
    }
    
    // Getters - ways to READ the data
    public String getName() {
        return name;
    }
    
    public int getAge() {
        return age;
    }
    
    public String getEmail() {
        return email;
    }
    
    // Setters - ways to WRITE the data
    public void setName(String name) {
        this.name = name;
    }
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    // toString - for printing the object nicely
    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + ", email='" + email + "'}";
    }
}
