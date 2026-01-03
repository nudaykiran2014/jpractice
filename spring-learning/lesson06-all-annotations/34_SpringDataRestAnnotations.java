package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 34: SPRING DATA REST ANNOTATIONS                                       ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Spring Data REST auto-exposes repositories as REST endpoints.
 * 
 * Requires: spring-boot-starter-data-rest
 */
public class _34_SpringDataRestAnnotations {
    public static void main(String[] args) {
        System.out.println("=== SPRING DATA REST ANNOTATIONS ===\n");
        System.out.println("@RepositoryRestResource → Customize REST endpoint");
        System.out.println("@RestResource       → Customize field/method exposure");
        System.out.println("@Projection         → Custom view of entity");
        System.out.println("@RepositoryEventHandler → Handle CRUD events");
        System.out.println("@HandleBeforeCreate → Before entity creation");
        System.out.println("@HandleAfterCreate  → After entity creation");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * AUTO-EXPOSED REPOSITORY
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Just create a repository - REST endpoints are auto-generated!
 * 
 * EXAMPLE:
 * ---------
 *     @Entity
 *     public class User {
 *         @Id @GeneratedValue
 *         private Long id;
 *         private String name;
 *         private String email;
 *     }
 *     
 *     public interface UserRepository extends JpaRepository<User, Long> { }
 *     
 *     // AUTO-GENERATED ENDPOINTS:
 *     // GET    /users       → List all users (paginated)
 *     // GET    /users/{id}  → Get single user
 *     // POST   /users       → Create user
 *     // PUT    /users/{id}  → Replace user
 *     // PATCH  /users/{id}  → Update user
 *     // DELETE /users/{id}  → Delete user
 *     // GET    /users/search/findByName?name=John → Custom query
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @RepositoryRestResource
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Customize the REST endpoint for a repository
 * 
 * EXAMPLE:
 * ---------
 *     @RepositoryRestResource(
 *         path = "people",              // /people instead of /users
 *         collectionResourceRel = "people",
 *         itemResourceRel = "person"
 *     )
 *     public interface UserRepository extends JpaRepository<User, Long> {
 *         
 *         // Exposed at: /people/search/findByEmail
 *         List<User> findByEmail(@Param("email") String email);
 *     }
 *     
 *     // Don't expose repository at all
 *     @RepositoryRestResource(exported = false)
 *     public interface InternalRepository extends JpaRepository<Internal, Long> { }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @RestResource
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Customize or hide specific methods
 * 
 * EXAMPLE:
 * ---------
 *     public interface UserRepository extends JpaRepository<User, Long> {
 *         
 *         // Custom path for search method
 *         @RestResource(path = "by-email", rel = "by-email")
 *         List<User> findByEmail(@Param("email") String email);
 *         
 *         // Hide this method from REST API
 *         @RestResource(exported = false)
 *         List<User> findByPassword(String password);
 *         
 *         // Hide delete operation
 *         @Override
 *         @RestResource(exported = false)
 *         void deleteById(Long id);
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Projection
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Custom views of entities (like DTOs)
 * 
 * EXAMPLE:
 * ---------
 *     @Entity
 *     public class User {
 *         @Id private Long id;
 *         private String firstName;
 *         private String lastName;
 *         private String email;
 *         private String password;  // Don't expose!
 *         
 *         @OneToMany
 *         private List<Order> orders;
 *     }
 *     
 *     // Projection - expose only specific fields
 *     @Projection(name = "summary", types = User.class)
 *     public interface UserSummary {
 *         String getFirstName();
 *         String getLastName();
 *         String getEmail();
 *         // password not included!
 *     }
 *     
 *     // Projection with computed property
 *     @Projection(name = "full", types = User.class)
 *     public interface UserFull {
 *         String getFirstName();
 *         String getLastName();
 *         
 *         @Value("#{target.firstName + ' ' + target.lastName}")
 *         String getFullName();  // Computed!
 *         
 *         List<Order> getOrders();  // Include relations
 *     }
 *     
 *     // Usage: GET /users?projection=summary
 *     //        GET /users/1?projection=full
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * EVENT HANDLERS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Execute code before/after CRUD operations
 * 
 * EXAMPLE:
 * ---------
 *     @Component
 *     @RepositoryEventHandler
 *     public class UserEventHandler {
 *         
 *         @HandleBeforeCreate
 *         public void handleBeforeCreate(User user) {
 *             // Validate, set defaults, etc.
 *             user.setCreatedAt(LocalDateTime.now());
 *             user.setStatus("PENDING");
 *         }
 *         
 *         @HandleAfterCreate
 *         public void handleAfterCreate(User user) {
 *             // Send welcome email, log, etc.
 *             emailService.sendWelcome(user);
 *         }
 *         
 *         @HandleBeforeSave
 *         public void handleBeforeSave(User user) {
 *             user.setUpdatedAt(LocalDateTime.now());
 *         }
 *         
 *         @HandleAfterSave
 *         public void handleAfterSave(User user) {
 *             auditService.log("User updated: " + user.getId());
 *         }
 *         
 *         @HandleBeforeDelete
 *         public void handleBeforeDelete(User user) {
 *             // Prevent deletion if conditions not met
 *             if (user.hasActiveOrders()) {
 *                 throw new RuntimeException("Cannot delete user with active orders");
 *             }
 *         }
 *         
 *         @HandleAfterDelete
 *         public void handleAfterDelete(User user) {
 *             cleanupService.cleanUserData(user.getId());
 *         }
 *         
 *         @HandleBeforeLinkSave
 *         public void handleBeforeLinkSave(User user, Order order) {
 *             // Before linking user to order
 *         }
 *         
 *         @HandleAfterLinkSave
 *         public void handleAfterLinkSave(User user, Order order) {
 *             // After linking
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * CONFIGURATION
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     public class RestConfig implements RepositoryRestConfigurer {
 *         
 *         @Override
 *         public void configureRepositoryRestConfiguration(
 *                 RepositoryRestConfiguration config, CorsRegistry cors) {
 *             
 *             // Change base path
 *             config.setBasePath("/api");  // /api/users instead of /users
 *             
 *             // Expose IDs in responses
 *             config.exposeIdsFor(User.class, Order.class);
 *             
 *             // Default page size
 *             config.setDefaultPageSize(20);
 *             config.setMaxPageSize(100);
 *             
 *             // CORS
 *             cors.addMapping("/**")
 *                 .allowedOrigins("*")
 *                 .allowedMethods("GET", "POST", "PUT", "DELETE");
 *         }
 *     }
 * 
 *     # application.properties
 *     spring.data.rest.base-path=/api
 *     spring.data.rest.default-page-size=20
 *     spring.data.rest.max-page-size=100
 * 
 * SPRING DATA REST vs @RestController:
 * --------------------------------------
 * | Feature          | Data REST    | @RestController  |
 * |------------------|--------------|------------------|
 * | Setup            | Zero code    | Manual           |
 * | Customization    | Limited      | Full control     |
 * | HATEOAS          | Built-in     | Manual           |
 * | Complex logic    | Limited      | Full flexibility |
 * | Best for         | Simple CRUD  | Complex APIs     |
 */
