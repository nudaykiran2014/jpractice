package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 19: OPENAPI / SWAGGER ANNOTATIONS                                      ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Document your REST APIs automatically with Swagger/OpenAPI.
 * 
 * Requires: springdoc-openapi-starter-webmvc-ui
 */
public class _19_OpenApiSwaggerAnnotations {
    public static void main(String[] args) {
        System.out.println("=== OPENAPI / SWAGGER ANNOTATIONS ===\n");
        System.out.println("@Tag           → Group/name controller");
        System.out.println("@Operation     → Describe endpoint");
        System.out.println("@Parameter     → Describe parameter");
        System.out.println("@ApiResponse   → Describe response");
        System.out.println("@Schema        → Describe model/field");
        System.out.println("@Hidden        → Hide from docs");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * CONTROLLER ANNOTATIONS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @RestController
 *     @RequestMapping("/api/users")
 *     @Tag(name = "User Management", description = "APIs for managing users")
 *     public class UserController {
 *         
 *         @Operation(
 *             summary = "Get all users",
 *             description = "Returns a list of all registered users"
 *         )
 *         @ApiResponses({
 *             @ApiResponse(responseCode = "200", description = "Success"),
 *             @ApiResponse(responseCode = "500", description = "Server error")
 *         })
 *         @GetMapping
 *         public List<User> getAllUsers() {
 *             return userService.findAll();
 *         }
 *         
 *         @Operation(summary = "Get user by ID")
 *         @ApiResponses({
 *             @ApiResponse(responseCode = "200", description = "User found"),
 *             @ApiResponse(responseCode = "404", description = "User not found")
 *         })
 *         @GetMapping("/{id}")
 *         public User getUser(
 *             @Parameter(description = "User ID", required = true, example = "123")
 *             @PathVariable Long id
 *         ) {
 *             return userService.findById(id);
 *         }
 *         
 *         @Operation(
 *             summary = "Create new user",
 *             description = "Creates a new user and returns the created object"
 *         )
 *         @ApiResponse(responseCode = "201", description = "User created")
 *         @PostMapping
 *         public User createUser(
 *             @io.swagger.v3.oas.annotations.parameters.RequestBody(
 *                 description = "User to create",
 *                 required = true
 *             )
 *             @RequestBody User user
 *         ) {
 *             return userService.save(user);
 *         }
 *         
 *         // Hide from documentation
 *         @Hidden
 *         @GetMapping("/internal")
 *         public String internal() {
 *             return "hidden";
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * MODEL / SCHEMA ANNOTATIONS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Document your DTOs and entities
 * 
 * EXAMPLE:
 * ---------
 *     @Schema(description = "User entity")
 *     public class User {
 *         
 *         @Schema(
 *             description = "Unique identifier",
 *             example = "123",
 *             accessMode = Schema.AccessMode.READ_ONLY
 *         )
 *         private Long id;
 *         
 *         @Schema(
 *             description = "User's full name",
 *             example = "John Doe",
 *             minLength = 2,
 *             maxLength = 100,
 *             required = true
 *         )
 *         private String name;
 *         
 *         @Schema(
 *             description = "Email address",
 *             example = "john@example.com",
 *             pattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"
 *         )
 *         private String email;
 *         
 *         @Schema(
 *             description = "User age",
 *             minimum = "0",
 *             maximum = "120"
 *         )
 *         private Integer age;
 *         
 *         @Schema(
 *             description = "Account status",
 *             allowableValues = {"ACTIVE", "INACTIVE", "SUSPENDED"}
 *         )
 *         private String status;
 *         
 *         @Schema(hidden = true)  // Hide from docs
 *         private String internalCode;
 *     }
 *     
 *     // For request DTOs
 *     @Schema(description = "User creation request")
 *     public class CreateUserRequest {
 *         
 *         @Schema(description = "Name", required = true)
 *         @NotBlank
 *         private String name;
 *         
 *         @Schema(description = "Email", required = true)
 *         @Email
 *         private String email;
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * GLOBAL CONFIGURATION
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     public class OpenApiConfig {
 *         
 *         @Bean
 *         public OpenAPI customOpenAPI() {
 *             return new OpenAPI()
 *                 .info(new Info()
 *                     .title("My Application API")
 *                     .version("1.0.0")
 *                     .description("REST API documentation")
 *                     .contact(new Contact()
 *                         .name("Dev Team")
 *                         .email("dev@example.com")
 *                     )
 *                     .license(new License()
 *                         .name("Apache 2.0")
 *                         .url("https://www.apache.org/licenses/LICENSE-2.0")
 *                     )
 *                 )
 *                 .externalDocs(new ExternalDocumentation()
 *                     .description("Wiki")
 *                     .url("https://wiki.example.com")
 *                 );
 *         }
 *     }
 *     
 *     // Security scheme for JWT
 *     @Configuration
 *     @SecurityScheme(
 *         name = "bearerAuth",
 *         type = SecuritySchemeType.HTTP,
 *         scheme = "bearer",
 *         bearerFormat = "JWT"
 *     )
 *     public class SecurityConfig { }
 *     
 *     // Apply security to controller
 *     @RestController
 *     @SecurityRequirement(name = "bearerAuth")
 *     public class SecureController { }
 * 
 * ACCESS SWAGGER UI:
 * -------------------
 *     http://localhost:8080/swagger-ui.html
 *     http://localhost:8080/v3/api-docs  (JSON)
 *     http://localhost:8080/v3/api-docs.yaml  (YAML)
 */
