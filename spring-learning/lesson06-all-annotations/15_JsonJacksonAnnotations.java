package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 15: JSON / JACKSON ANNOTATIONS                                         ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Jackson is the default JSON library in Spring Boot.
 * These annotations control how objects are serialized/deserialized to/from JSON.
 */
public class _15_JsonJacksonAnnotations {
    public static void main(String[] args) {
        System.out.println("=== JSON / JACKSON ANNOTATIONS ===\n");
        System.out.println("@JsonProperty     → Custom JSON property name");
        System.out.println("@JsonIgnore       → Exclude field from JSON");
        System.out.println("@JsonIgnoreProperties → Ignore multiple/unknown props");
        System.out.println("@JsonFormat       → Format dates/numbers");
        System.out.println("@JsonInclude      → Include non-null only");
        System.out.println("@JsonNaming       → Naming strategy (snake_case)");
        System.out.println("@JsonCreator      → Custom deserialization constructor");
        System.out.println("@JsonValue/@JsonGetter → Custom serialization");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * @JsonProperty
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Map JSON property name to Java field name
 * 
 * EXAMPLE:
 * ---------
 *     public class User {
 *         
 *         @JsonProperty("user_name")  // JSON: {"user_name": "John"}
 *         private String userName;    // Java: userName
 *         
 *         @JsonProperty("email_address")
 *         private String email;
 *         
 *         // Read-only (only for deserialization)
 *         @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
 *         private String password;  // Won't appear in JSON response
 *         
 *         // Write-only (only for serialization)
 *         @JsonProperty(access = JsonProperty.Access.READ_ONLY)
 *         private String createdAt;  // Can't be set from JSON input
 *         
 *         // Required field
 *         @JsonProperty(required = true)
 *         private String id;
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @JsonIgnore and @JsonIgnoreProperties
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Exclude fields from JSON serialization/deserialization
 * 
 * EXAMPLE:
 * ---------
 *     public class User {
 *         
 *         private String name;
 *         
 *         @JsonIgnore  // Never in JSON
 *         private String password;
 *         
 *         @JsonIgnore
 *         private String internalId;
 *     }
 *     
 *     // Ignore multiple + unknown properties
 *     @JsonIgnoreProperties({"password", "internalId"})
 *     public class User {
 *         private String name;
 *         private String password;
 *         private String internalId;
 *     }
 *     
 *     // Ignore unknown properties (don't fail on extra JSON fields)
 *     @JsonIgnoreProperties(ignoreUnknown = true)
 *     public class User {
 *         private String name;
 *         // Won't fail if JSON has extra fields
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @JsonFormat
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Format dates, times, and numbers in JSON
 * 
 * EXAMPLE:
 * ---------
 *     public class Order {
 *         
 *         // Date format
 *         @JsonFormat(pattern = "yyyy-MM-dd")
 *         private LocalDate orderDate;  // "2024-01-15"
 *         
 *         // DateTime format
 *         @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
 *         private LocalDateTime createdAt;  // "2024-01-15 14:30:00"
 *         
 *         // With timezone
 *         @JsonFormat(
 *             pattern = "yyyy-MM-dd HH:mm:ss",
 *             timezone = "America/New_York"
 *         )
 *         private Date timestamp;
 *         
 *         // Number format
 *         @JsonFormat(shape = JsonFormat.Shape.STRING)
 *         private BigDecimal amount;  // "123.45" (as string)
 *         
 *         // Enum as number
 *         @JsonFormat(shape = JsonFormat.Shape.NUMBER)
 *         private Status status;  // 0, 1, 2 instead of "PENDING"
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @JsonInclude
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Control when fields are included in JSON
 * 
 * EXAMPLE:
 * ---------
 *     // Class level
 *     @JsonInclude(JsonInclude.Include.NON_NULL)  // Exclude null fields
 *     public class User {
 *         private String name;
 *         private String email;  // Won't appear if null
 *     }
 *     
 *     // Field level
 *     public class Response {
 *         
 *         @JsonInclude(JsonInclude.Include.NON_NULL)
 *         private String error;  // Only if not null
 *         
 *         @JsonInclude(JsonInclude.Include.NON_EMPTY)
 *         private List<String> items;  // Only if not empty
 *         
 *         @JsonInclude(JsonInclude.Include.NON_DEFAULT)
 *         private int count;  // Only if not 0
 *     }
 * 
 * OPTIONS:
 * | Include      | Excludes                    |
 * |--------------|----------------------------|
 * | ALWAYS       | Nothing (default)          |
 * | NON_NULL     | Null values                |
 * | NON_EMPTY    | Null, empty strings/collections |
 * | NON_DEFAULT  | Default values (0, false)  |
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @JsonNaming
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Apply naming strategy to all fields
 * 
 * EXAMPLE:
 * ---------
 *     // All fields become snake_case
 *     @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
 *     public class User {
 *         private String firstName;  // → "first_name"
 *         private String lastName;   // → "last_name"
 *         private String emailAddress;  // → "email_address"
 *     }
 *     
 *     // Kebab case
 *     @JsonNaming(PropertyNamingStrategies.KebabCaseStrategy.class)
 *     public class Config {
 *         private String maxRetryCount;  // → "max-retry-count"
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @JsonCreator and @JsonValue
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Custom serialization/deserialization
 * 
 * EXAMPLE:
 * ---------
 *     public class Money {
 *         private BigDecimal amount;
 *         private String currency;
 *         
 *         // Custom deserialization
 *         @JsonCreator
 *         public Money(
 *             @JsonProperty("amount") BigDecimal amount,
 *             @JsonProperty("currency") String currency
 *         ) {
 *             this.amount = amount;
 *             this.currency = currency;
 *         }
 *         
 *         // Custom serialization (entire object as single value)
 *         @JsonValue
 *         public String toJson() {
 *             return amount + " " + currency;  // "100.00 USD"
 *         }
 *     }
 *     
 *     // Enum with custom value
 *     public enum Status {
 *         ACTIVE("A"),
 *         INACTIVE("I");
 *         
 *         private String code;
 *         
 *         Status(String code) { this.code = code; }
 *         
 *         @JsonValue
 *         public String getCode() { return code; }  // "A" or "I"
 *         
 *         @JsonCreator
 *         public static Status fromCode(String code) {
 *             // Convert "A" back to ACTIVE
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @JsonGetter and @JsonSetter
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Custom getter/setter for JSON
 * 
 * EXAMPLE:
 * ---------
 *     public class User {
 *         private String firstName;
 *         private String lastName;
 *         
 *         // Custom JSON property from method
 *         @JsonGetter("full_name")
 *         public String getFullName() {
 *             return firstName + " " + lastName;
 *         }
 *         
 *         @JsonSetter("full_name")
 *         public void setFullName(String fullName) {
 *             String[] parts = fullName.split(" ");
 *             this.firstName = parts[0];
 *             this.lastName = parts[1];
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @JsonAlias and @JsonAnyGetter/@JsonAnySetter
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     public class User {
 *         
 *         // Accept multiple names during deserialization
 *         @JsonAlias({"user_name", "username", "login"})
 *         private String userName;
 *         
 *         // Catch-all for unknown properties
 *         private Map<String, Object> additionalProperties = new HashMap<>();
 *         
 *         @JsonAnySetter
 *         public void setAdditional(String key, Object value) {
 *             additionalProperties.put(key, value);
 *         }
 *         
 *         @JsonAnyGetter
 *         public Map<String, Object> getAdditional() {
 *             return additionalProperties;
 *         }
 *     }
 */
