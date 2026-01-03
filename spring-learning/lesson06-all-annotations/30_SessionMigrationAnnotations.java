package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 30: SESSION & DATABASE MIGRATION ANNOTATIONS                           ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Session management and database migration tools.
 */
public class _30_SessionMigrationAnnotations {
    public static void main(String[] args) {
        System.out.println("=== SESSION & MIGRATION ANNOTATIONS ===\n");
        System.out.println("SPRING SESSION:");
        System.out.println("  @EnableRedisHttpSession → Redis sessions");
        System.out.println("  @EnableJdbcHttpSession  → JDBC sessions");
        System.out.println("  @SessionAttributes      → Store in session");
        System.out.println("  @SessionScope           → Session-scoped bean");
        System.out.println("\nFLYWAY:");
        System.out.println("  Auto-configured via naming convention");
        System.out.println("  V1__Initial_schema.sql");
        System.out.println("\nLIQUIBASE:");
        System.out.println("  changelog.xml/yaml configuration");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * SPRING SESSION
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Externalize session storage (for scaling/clustering)
 * 
 * EXAMPLE - REDIS SESSION:
 * -------------------------
 *     // Requires: spring-session-data-redis
 *     @Configuration
 *     @EnableRedisHttpSession(maxInactiveIntervalInSeconds = 3600)  // 1 hour
 *     public class SessionConfig {
 *         
 *         @Bean
 *         public LettuceConnectionFactory connectionFactory() {
 *             return new LettuceConnectionFactory();
 *         }
 *     }
 *     
 *     // application.properties
 *     spring.session.store-type=redis
 *     spring.redis.host=localhost
 *     spring.redis.port=6379
 * 
 * EXAMPLE - JDBC SESSION:
 * ------------------------
 *     // Requires: spring-session-jdbc
 *     @Configuration
 *     @EnableJdbcHttpSession(maxInactiveIntervalInSeconds = 1800)  // 30 min
 *     public class SessionConfig { }
 *     
 *     // application.properties
 *     spring.session.store-type=jdbc
 *     spring.session.jdbc.initialize-schema=always
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @SessionAttributes
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Store model attributes in HTTP session
 * 
 * EXAMPLE:
 * ---------
 *     @Controller
 *     @SessionAttributes({"cart", "user"})  // These attributes stored in session
 *     public class ShoppingController {
 *         
 *         @ModelAttribute("cart")
 *         public ShoppingCart createCart() {
 *             return new ShoppingCart();  // Created once, stored in session
 *         }
 *         
 *         @PostMapping("/add-item")
 *         public String addItem(@ModelAttribute("cart") ShoppingCart cart, 
 *                              @RequestParam Long productId) {
 *             cart.addItem(productId);
 *             return "redirect:/cart";
 *         }
 *         
 *         @PostMapping("/checkout")
 *         public String checkout(@ModelAttribute("cart") ShoppingCart cart,
 *                               SessionStatus status) {
 *             orderService.placeOrder(cart);
 *             status.setComplete();  // Clear session attributes
 *             return "redirect:/order-confirmation";
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @SessionScope
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Create bean that lives for entire user session
 * 
 * EXAMPLE:
 * ---------
 *     @Component
 *     @SessionScope
 *     public class UserPreferences {
 *         private String theme = "light";
 *         private String language = "en";
 *         
 *         // Getters and setters
 *     }
 *     
 *     @Controller
 *     public class PreferencesController {
 *         
 *         @Autowired
 *         private UserPreferences preferences;  // Same instance for session
 *         
 *         @PostMapping("/set-theme")
 *         public String setTheme(@RequestParam String theme) {
 *             preferences.setTheme(theme);  // Persists for session
 *             return "redirect:/";
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * FLYWAY (Database Migration)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Version control for database schema
 * 
 * Requires: flyway-core
 * 
 * FILE NAMING CONVENTION:
 * ------------------------
 *     src/main/resources/db/migration/
 *     ├── V1__Create_users_table.sql
 *     ├── V2__Add_email_column.sql
 *     ├── V3__Create_orders_table.sql
 *     └── V4__Add_index.sql
 *     
 *     V = Versioned migration
 *     1 = Version number
 *     __ = Separator (two underscores)
 *     Description = What this migration does
 * 
 * EXAMPLE MIGRATION FILES:
 * -------------------------
 *     -- V1__Create_users_table.sql
 *     CREATE TABLE users (
 *         id BIGINT PRIMARY KEY AUTO_INCREMENT,
 *         name VARCHAR(100) NOT NULL,
 *         email VARCHAR(255) UNIQUE NOT NULL,
 *         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
 *     );
 *     
 *     -- V2__Add_status_column.sql
 *     ALTER TABLE users ADD COLUMN status VARCHAR(20) DEFAULT 'ACTIVE';
 *     
 *     -- V3__Create_orders_table.sql
 *     CREATE TABLE orders (
 *         id BIGINT PRIMARY KEY AUTO_INCREMENT,
 *         user_id BIGINT NOT NULL,
 *         total DECIMAL(10,2),
 *         FOREIGN KEY (user_id) REFERENCES users(id)
 *     );
 * 
 * CONFIGURATION:
 * ---------------
 *     # application.properties
 *     spring.flyway.enabled=true
 *     spring.flyway.locations=classpath:db/migration
 *     spring.flyway.baseline-on-migrate=true
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * LIQUIBASE (Database Migration)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Another database migration tool (more features than Flyway)
 * 
 * Requires: liquibase-core
 * 
 * EXAMPLE - changelog.yaml:
 * --------------------------
 *     # src/main/resources/db/changelog/db.changelog-master.yaml
 *     databaseChangeLog:
 *       - changeSet:
 *           id: 1
 *           author: john
 *           changes:
 *             - createTable:
 *                 tableName: users
 *                 columns:
 *                   - column:
 *                       name: id
 *                       type: BIGINT
 *                       autoIncrement: true
 *                       constraints:
 *                         primaryKey: true
 *                   - column:
 *                       name: name
 *                       type: VARCHAR(100)
 *                       constraints:
 *                         nullable: false
 *       
 *       - changeSet:
 *           id: 2
 *           author: john
 *           changes:
 *             - addColumn:
 *                 tableName: users
 *                 columns:
 *                   - column:
 *                       name: email
 *                       type: VARCHAR(255)
 * 
 * EXAMPLE - changelog.xml:
 * -------------------------
 *     <?xml version="1.0" encoding="UTF-8"?>
 *     <databaseChangeLog xmlns="http://www.liquibase.org/xml/ns/dbchangelog">
 *         
 *         <changeSet id="1" author="john">
 *             <createTable tableName="users">
 *                 <column name="id" type="BIGINT" autoIncrement="true">
 *                     <constraints primaryKey="true"/>
 *                 </column>
 *                 <column name="name" type="VARCHAR(100)">
 *                     <constraints nullable="false"/>
 *                 </column>
 *             </createTable>
 *         </changeSet>
 *         
 *     </databaseChangeLog>
 * 
 * CONFIGURATION:
 * ---------------
 *     # application.properties
 *     spring.liquibase.change-log=classpath:db/changelog/db.changelog-master.yaml
 *     spring.liquibase.enabled=true
 * 
 * FLYWAY vs LIQUIBASE:
 * ---------------------
 * | Feature        | Flyway          | Liquibase        |
 * |----------------|-----------------|------------------|
 * | Format         | SQL files       | XML/YAML/JSON/SQL|
 * | Rollback       | Paid feature    | Built-in         |
 * | Complexity     | Simple          | More complex     |
 * | DB support     | Many            | More databases   |
 * | Learning curve | Easy            | Moderate         |
 */
