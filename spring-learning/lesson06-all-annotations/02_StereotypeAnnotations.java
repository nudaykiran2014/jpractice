package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 2: STEREOTYPE ANNOTATIONS (Bean Registration)                          ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * These annotations REGISTER a class as a Spring bean.
 * Spring will create and manage instances of these classes.
 */
public class _02_StereotypeAnnotations {
    public static void main(String[] args) {
        System.out.println("=== STEREOTYPE ANNOTATIONS ===\n");
        System.out.println("@Component  → Generic bean");
        System.out.println("@Service    → Business logic layer");
        System.out.println("@Repository → Data access layer");
        System.out.println("@Controller → Web MVC (returns HTML)");
        System.out.println("@RestController → REST API (returns JSON)");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Component
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Generic annotation to mark a class as a Spring-managed bean
 *          Use when the class doesn't fit into Service/Repository/Controller
 * 
 * WHERE: Any class you want Spring to manage
 * 
 * EXAMPLE:
 * ---------
 *     @Component
 *     public class EmailValidator {
 *         public boolean isValid(String email) {
 *             return email != null && email.contains("@");
 *         }
 *     }
 *     
 *     // With custom bean name:
 *     @Component("myValidator")
 *     public class EmailValidator { }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Service
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Marks a class as a SERVICE (business logic layer)
 *          Functionally same as @Component, but more semantic/readable
 * 
 * WHERE: Classes containing business logic
 * 
 * EXAMPLE:
 * ---------
 *     @Service
 *     public class UserService {
 *         
 *         @Autowired
 *         private UserRepository userRepository;
 *         
 *         public User createUser(String name, String email) {
 *             User user = new User(name, email);
 *             return userRepository.save(user);
 *         }
 *         
 *         public List<User> findAllActiveUsers() {
 *             return userRepository.findByActiveTrue();
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Repository
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Marks a class as a DATA ACCESS component (DAO layer)
 *          SPECIAL: Automatically translates database exceptions to Spring's 
 *                   DataAccessException hierarchy
 * 
 * WHERE: Classes that interact with databases
 * 
 * EXAMPLE:
 * ---------
 *     // With Spring Data JPA (most common):
 *     @Repository
 *     public interface UserRepository extends JpaRepository<User, Long> {
 *         List<User> findByEmail(String email);
 *         
 *         @Query("SELECT u FROM User u WHERE u.active = true")
 *         List<User> findActiveUsers();
 *     }
 *     
 *     // Without Spring Data (manual implementation):
 *     @Repository
 *     public class UserRepositoryImpl {
 *         @Autowired
 *         private JdbcTemplate jdbcTemplate;
 *         
 *         public User findById(Long id) {
 *             return jdbcTemplate.queryForObject(
 *                 "SELECT * FROM users WHERE id = ?",
 *                 new UserRowMapper(), id
 *             );
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Controller
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Marks a class as a WEB CONTROLLER (MVC - returns views/HTML)
 *          Methods return view names, which are resolved to HTML templates
 * 
 * WHERE: Classes that handle HTTP requests and return HTML pages
 * 
 * EXAMPLE:
 * ---------
 *     @Controller
 *     public class HomeController {
 *         
 *         @GetMapping("/")
 *         public String home(Model model) {
 *             model.addAttribute("message", "Welcome!");
 *             return "home";  // Returns home.html template
 *         }
 *         
 *         @GetMapping("/users")
 *         public String listUsers(Model model) {
 *             model.addAttribute("users", userService.findAll());
 *             return "users/list";  // Returns users/list.html
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @RestController
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Marks a class as a REST API CONTROLLER (returns JSON/XML)
 *          Combines @Controller + @ResponseBody
 *          Every method automatically serializes return value to JSON
 * 
 * WHERE: Classes that handle REST API endpoints
 * 
 * EXAMPLE:
 * ---------
 *     @RestController
 *     @RequestMapping("/api/users")
 *     public class UserRestController {
 *         
 *         @Autowired
 *         private UserService userService;
 *         
 *         @GetMapping
 *         public List<User> getAllUsers() {
 *             return userService.findAll();  // Automatically converted to JSON
 *         }
 *         
 *         @GetMapping("/{id}")
 *         public User getUser(@PathVariable Long id) {
 *             return userService.findById(id);
 *         }
 *         
 *         @PostMapping
 *         @ResponseStatus(HttpStatus.CREATED)
 *         public User createUser(@RequestBody User user) {
 *             return userService.save(user);
 *         }
 *         
 *         @DeleteMapping("/{id}")
 *         @ResponseStatus(HttpStatus.NO_CONTENT)
 *         public void deleteUser(@PathVariable Long id) {
 *             userService.deleteById(id);
 *         }
 *     }
 */
