package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 5: VALIDATION ANNOTATIONS                                              ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Requires: spring-boot-starter-validation dependency
 */
public class _05_ValidationAnnotations {
    public static void main(String[] args) {
        System.out.println("=== VALIDATION ANNOTATIONS ===\n");
        System.out.println("@Valid      → Trigger validation on object");
        System.out.println("@NotNull    → Field cannot be null");
        System.out.println("@NotEmpty   → String/collection cannot be empty");
        System.out.println("@NotBlank   → String cannot be blank");
        System.out.println("@Size       → Length limits");
        System.out.println("@Min/@Max   → Numeric range");
        System.out.println("@Email      → Valid email format");
        System.out.println("@Pattern    → Regex pattern match");
        System.out.println("@Past/@Future → Date constraints");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Valid and @Validated
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Triggers validation on an object
 * 
 * WHERE: On @RequestBody parameters
 * 
 * EXAMPLE:
 * ---------
 *     @PostMapping("/users")
 *     public User create(@RequestBody @Valid UserRequest request) {
 *         // Validation happens before this method
 *         // If validation fails, 400 Bad Request returned
 *         return userService.create(request);
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * FIELD VALIDATION ANNOTATIONS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     public class UserCreateRequest {
 *         
 *         @NotNull(message = "Name is required")
 *         @Size(min = 2, max = 50, message = "Name must be 2-50 characters")
 *         private String name;
 *         
 *         @NotBlank(message = "Email cannot be blank")
 *         @Email(message = "Invalid email format")
 *         private String email;
 *         
 *         @NotNull
 *         @Min(value = 18, message = "Must be at least 18")
 *         @Max(value = 120, message = "Age cannot exceed 120")
 *         private Integer age;
 *         
 *         @NotEmpty(message = "At least one role required")
 *         private List<String> roles;
 *         
 *         @Pattern(regexp = "^\\+?[1-9]\\d{9,14}$", message = "Invalid phone")
 *         private String phone;
 *         
 *         @Past(message = "Birth date must be in the past")
 *         private LocalDate birthDate;
 *         
 *         @Future(message = "Expiry must be in the future")
 *         private LocalDate expiryDate;
 *         
 *         @Positive(message = "Amount must be positive")
 *         private BigDecimal amount;
 *         
 *         @PositiveOrZero
 *         private Integer quantity;
 *         
 *         @DecimalMin("0.0") @DecimalMax("100.0")
 *         private Double percentage;
 *         
 *         // Nested validation
 *         @Valid
 *         @NotNull
 *         private Address address;
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * HANDLING VALIDATION ERRORS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @RestControllerAdvice
 *     public class ValidationHandler {
 *         
 *         @ExceptionHandler(MethodArgumentNotValidException.class)
 *         @ResponseStatus(HttpStatus.BAD_REQUEST)
 *         public Map<String, String> handleValidation(
 *                 MethodArgumentNotValidException ex) {
 *             Map<String, String> errors = new HashMap<>();
 *             ex.getBindingResult().getFieldErrors().forEach(error ->
 *                 errors.put(error.getField(), error.getDefaultMessage())
 *             );
 *             return errors;
 *         }
 *     }
 * 
 * VALIDATION ANNOTATIONS SUMMARY:
 * --------------------------------
 * | Annotation      | Purpose                              |
 * |-----------------|--------------------------------------|
 * | @NotNull        | Cannot be null                       |
 * | @NotEmpty       | Cannot be null or empty              |
 * | @NotBlank       | Cannot be null, empty, or whitespace |
 * | @Size           | String/collection length limits      |
 * | @Min / @Max     | Numeric min/max                      |
 * | @Positive       | Must be > 0                          |
 * | @PositiveOrZero | Must be >= 0                         |
 * | @Negative       | Must be < 0                          |
 * | @Email          | Valid email format                   |
 * | @Pattern        | Matches regex                        |
 * | @Past           | Date in past                         |
 * | @Future         | Date in future                       |
 * | @PastOrPresent  | Date in past or today                |
 * | @FutureOrPresent| Date in future or today              |
 */
