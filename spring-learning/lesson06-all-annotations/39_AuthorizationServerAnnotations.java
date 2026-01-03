package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 39: SPRING AUTHORIZATION SERVER & OAUTH2 ANNOTATIONS                   ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Build OAuth2/OIDC authorization servers and resource servers.
 * 
 * Requires: spring-boot-starter-oauth2-authorization-server
 *           spring-boot-starter-oauth2-resource-server
 */
public class _39_AuthorizationServerAnnotations {
    public static void main(String[] args) {
        System.out.println("=== OAUTH2 / AUTHORIZATION SERVER ===\n");
        System.out.println("AUTHORIZATION SERVER:");
        System.out.println("  @EnableAuthorizationServer → Custom auth server");
        System.out.println("\nRESOURCE SERVER:");
        System.out.println("  @EnableResourceServer → Protect API with tokens");
        System.out.println("  @EnableWebSecurity    → Security configuration");
        System.out.println("\nMETHOD SECURITY:");
        System.out.println("  @PreAuthorize   → Check before method");
        System.out.println("  @PostAuthorize  → Check after method");
        System.out.println("  @AuthenticationPrincipal → Get current user");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * OAUTH2 RESOURCE SERVER
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Protect your API with JWT tokens
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     @EnableWebSecurity
 *     public class SecurityConfig {
 *         
 *         @Bean
 *         public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
 *             http
 *                 .authorizeHttpRequests(auth -> auth
 *                     .requestMatchers("/public/**").permitAll()
 *                     .requestMatchers("/api/admin/**").hasRole("ADMIN")
 *                     .anyRequest().authenticated()
 *                 )
 *                 .oauth2ResourceServer(oauth2 -> oauth2
 *                     .jwt(Customizer.withDefaults())  // Validate JWT tokens
 *                 );
 *             return http.build();
 *         }
 *     }
 *     
 *     # application.properties
 *     spring.security.oauth2.resourceserver.jwt.issuer-uri=https://auth.example.com
 *     # OR
 *     spring.security.oauth2.resourceserver.jwt.jwk-set-uri=https://auth.example.com/.well-known/jwks.json
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @AuthenticationPrincipal
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Get current authenticated user in controller
 * 
 * EXAMPLE:
 * ---------
 *     @RestController
 *     public class UserController {
 *         
 *         // Get JWT claims
 *         @GetMapping("/me")
 *         public Map<String, Object> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
 *             return Map.of(
 *                 "subject", jwt.getSubject(),
 *                 "email", jwt.getClaim("email"),
 *                 "roles", jwt.getClaimAsStringList("roles")
 *             );
 *         }
 *         
 *         // Get custom user details
 *         @GetMapping("/profile")
 *         public UserProfile getProfile(@AuthenticationPrincipal CustomUser user) {
 *             return userService.getProfile(user.getId());
 *         }
 *         
 *         // Extract specific claim
 *         @GetMapping("/email")
 *         public String getEmail(
 *             @AuthenticationPrincipal(expression = "claims['email']") String email
 *         ) {
 *             return email;
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * METHOD SECURITY WITH OAUTH2
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     @EnableMethodSecurity
 *     public class MethodSecurityConfig { }
 *     
 *     @Service
 *     public class OrderService {
 *         
 *         // Check JWT scope
 *         @PreAuthorize("hasAuthority('SCOPE_read:orders')")
 *         public List<Order> getOrders() {
 *             return orderRepository.findAll();
 *         }
 *         
 *         // Check JWT claim
 *         @PreAuthorize("#jwt.claims['department'] == 'sales'")
 *         public void createOrder(@AuthenticationPrincipal Jwt jwt, Order order) {
 *             orderRepository.save(order);
 *         }
 *         
 *         // Owner-only access
 *         @PreAuthorize("#order.userId == authentication.name")
 *         public void deleteOrder(Order order) {
 *             orderRepository.delete(order);
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * SPRING AUTHORIZATION SERVER (Build Your Own)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Create OAuth2/OIDC authorization server
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     public class AuthServerConfig {
 *         
 *         @Bean
 *         @Order(1)
 *         public SecurityFilterChain authServerFilterChain(HttpSecurity http) throws Exception {
 *             OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
 *             http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
 *                 .oidc(Customizer.withDefaults());  // Enable OpenID Connect
 *             return http.build();
 *         }
 *         
 *         @Bean
 *         public RegisteredClientRepository registeredClientRepository() {
 *             RegisteredClient client = RegisteredClient.withId(UUID.randomUUID().toString())
 *                 .clientId("my-client")
 *                 .clientSecret("{noop}secret")
 *                 .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
 *                 .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
 *                 .redirectUri("http://localhost:8080/callback")
 *                 .scope(OidcScopes.OPENID)
 *                 .scope("read")
 *                 .scope("write")
 *                 .build();
 *             return new InMemoryRegisteredClientRepository(client);
 *         }
 *         
 *         @Bean
 *         public JWKSource<SecurityContext> jwkSource() {
 *             RSAKey rsaKey = generateRsaKey();
 *             JWKSet jwkSet = new JWKSet(rsaKey);
 *             return (jwkSelector, context) -> jwkSelector.select(jwkSet);
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * OAUTH2 CLIENT (Consume External Auth)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Login with Google, GitHub, etc.
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     @EnableWebSecurity
 *     public class OAuth2LoginConfig {
 *         
 *         @Bean
 *         public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
 *             http
 *                 .authorizeHttpRequests(auth -> auth
 *                     .anyRequest().authenticated()
 *                 )
 *                 .oauth2Login(Customizer.withDefaults());  // Enable OAuth2 login
 *             return http.build();
 *         }
 *     }
 *     
 *     # application.properties
 *     spring.security.oauth2.client.registration.google.client-id=xxx
 *     spring.security.oauth2.client.registration.google.client-secret=xxx
 *     
 *     spring.security.oauth2.client.registration.github.client-id=xxx
 *     spring.security.oauth2.client.registration.github.client-secret=xxx
 *     
 *     // Controller
 *     @RestController
 *     public class UserController {
 *         
 *         @GetMapping("/user")
 *         public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
 *             return Map.of(
 *                 "name", principal.getAttribute("name"),
 *                 "email", principal.getAttribute("email")
 *             );
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * CUSTOM JWT CLAIMS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @Bean
 *     public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
 *         return context -> {
 *             if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
 *                 context.getClaims().claims(claims -> {
 *                     claims.put("custom_claim", "custom_value");
 *                     claims.put("roles", getUserRoles(context.getPrincipal()));
 *                 });
 *             }
 *         };
 *     }
 * 
 * OAUTH2 FLOWS:
 * --------------
 * | Flow                | Use Case                    |
 * |---------------------|-----------------------------| 
 * | Authorization Code  | Web apps with backend       |
 * | PKCE                | SPAs, mobile apps           |
 * | Client Credentials  | Service-to-service          |
 * | Refresh Token       | Long-lived access           |
 */
