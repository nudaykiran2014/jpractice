package spring_learning.lesson04_ioc_container;

/**
 * A simple repository (data access class)
 * In real Spring: @Repository
 */
public class UserRepository {
    
    public UserRepository() {
        System.out.println("   🔨 UserRepository instance created!");
    }
    
    public void save(String username) {
        System.out.println("   💾 Saved user: " + username);
    }
    
    public String findById(String id) {
        return "User_" + id;
    }
}
