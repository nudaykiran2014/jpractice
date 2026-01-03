package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 9: SECURITY ANNOTATIONS                                                ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Requires: spring-boot-starter-security
 */
public class _09_SecurityAnnotations {
    public static void main(String[] args) {
        System.out.println("=== SECURITY ANNOTATIONS ===\n");
        System.out.println("@EnableWebSecurity → Enable Spring Security");
        System.out.println("@EnableMethodSecurity → Enable method-level security");
        System.out.println("@PreAuthorize      → Check before method execution");
        System.out.println("@PostAuthorize     → Check after method execution");
        System.out.println("@Secured           → Simple role check");
        System.out.println("@RolesAllowed      → JSR-250 role check");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * SECURITY CONFIGURATION
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     @EnableWebSecurity
 *     @EnableMethodSecurity  // Enable @PreAuthorize, @PostAuthorize
 *     public class SecurityConfig {
 *         
 *         @Bean
 *         public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
 *             http
 *                 .authorizeHttpRequests(auth -> auth
 *                     .requestMatchers("/public/**").permitAll()
 *                     .requestMatchers("/admin/**").hasRole("ADMIN")
 *                     .anyRequest().authenticated()
 *                 )
 *                 .httpBasic(Customizer.withDefaults());
 *             return http.build();
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * METHOD SECURITY ANNOTATIONS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @RestController
 *     @RequestMapping("/api")
 *     public class SecureController {
 *         
 *         // Only users with ADMIN role
 *         @PreAuthorize("hasRole('ADMIN')")
 *         @GetMapping("/admin/users")
 *         public List<User> getAllUsers() { }
 *         
 *         // Only the user themselves or admin
 *         @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
 *         @GetMapping("/users/{id}")
 *         public User getUser(@PathVariable Long id) { }
 *         
 *         // Check permission/authority
 *         @PreAuthorize("hasAuthority('READ_DATA')")
 *         @GetMapping("/data")
 *         public Data getData() { }
 *         
 *         // Post-authorization (filter result after execution)
 *         @PostAuthorize("returnObject.owner == authentication.name")
 *         @GetMapping("/documents/{id}")
 *         public Document getDocument(@PathVariable Long id) { }
 *         
 *         // @Secured - simpler, less flexible
 *         @Secured("ROLE_ADMIN")
 *         @DeleteMapping("/users/{id}")
 *         public void deleteUser(@PathVariable Long id) { }
 *         
 *         // @RolesAllowed - JSR-250 standard
 *         @RolesAllowed({"ADMIN", "MANAGER"})
 *         @PostMapping("/reports")
 *         public Report createReport() { }
 *         
 *         // Multiple conditions
 *         @PreAuthorize("hasRole('ADMIN') and #request.amount < 10000")
 *         @PostMapping("/transfer")
 *         public void transfer(@RequestBody TransferRequest request) { }
 *     }
 * 
 * SECURITY EXPRESSIONS:
 * ----------------------
 * | Expression                    | Meaning                        |
 * |-------------------------------|--------------------------------|
 * | hasRole('ADMIN')              | Has ROLE_ADMIN                 |
 * | hasAnyRole('ADMIN','USER')    | Has any of these roles         |
 * | hasAuthority('READ')          | Has specific authority         |
 * | isAuthenticated()             | User is authenticated          |
 * | isAnonymous()                 | User is anonymous              |
 * | permitAll()                   | Always allow                   |
 * | denyAll()                     | Always deny                    |
 * | #paramName                    | Access method parameter        |
 * | authentication.principal      | Current user principal         |
 * | returnObject                  | Method return value (Post)     |
 */
