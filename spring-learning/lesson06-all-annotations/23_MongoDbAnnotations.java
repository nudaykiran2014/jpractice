package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 23: MONGODB ANNOTATIONS                                                ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Spring Data MongoDB for document-based NoSQL storage.
 * 
 * Requires: spring-boot-starter-data-mongodb
 */
public class _23_MongoDbAnnotations {
    public static void main(String[] args) {
        System.out.println("=== MONGODB ANNOTATIONS ===\n");
        System.out.println("@Document       → Mark class as MongoDB document");
        System.out.println("@Id             → Document identifier");
        System.out.println("@Field          → Customize field name");
        System.out.println("@Indexed        → Create index on field");
        System.out.println("@CompoundIndex  → Multi-field index");
        System.out.println("@DBRef          → Reference another document");
        System.out.println("@Transient      → Don't persist field");
        System.out.println("@TextIndexed    → Full-text search index");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Document
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Mark a class as a MongoDB document (like @Entity for JPA)
 * 
 * EXAMPLE:
 * ---------
 *     @Document(collection = "users")  // Collection name
 *     public class User {
 *         
 *         @Id
 *         private String id;  // MongoDB ObjectId
 *         
 *         private String name;
 *         private String email;
 *     }
 *     
 *     // With custom collection settings
 *     @Document(
 *         collection = "products",
 *         collation = @Collation(locale = "en", strength = 2)
 *     )
 *     public class Product { }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Field
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Customize field name in MongoDB document
 * 
 * EXAMPLE:
 * ---------
 *     @Document(collection = "users")
 *     public class User {
 *         
 *         @Id
 *         private String id;
 *         
 *         @Field("user_name")  // Stored as "user_name" in MongoDB
 *         private String userName;
 *         
 *         @Field(name = "email_address", order = 1)
 *         private String email;
 *         
 *         @Field(targetType = FieldType.DATE_TIME)
 *         private LocalDateTime createdAt;
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * INDEX ANNOTATIONS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @Document(collection = "users")
 *     @CompoundIndex(name = "name_email_idx", def = "{'name': 1, 'email': 1}")
 *     public class User {
 *         
 *         @Id
 *         private String id;
 *         
 *         @Indexed  // Single field index
 *         private String name;
 *         
 *         @Indexed(unique = true)  // Unique index
 *         private String email;
 *         
 *         @Indexed(expireAfterSeconds = 3600)  // TTL index - auto-delete after 1 hour
 *         private Date tempToken;
 *         
 *         @TextIndexed(weight = 2)  // Full-text search with weight
 *         private String title;
 *         
 *         @TextIndexed
 *         private String description;
 *         
 *         @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
 *         private Point location;
 *     }
 *     
 *     // Multiple compound indexes
 *     @Document
 *     @CompoundIndexes({
 *         @CompoundIndex(name = "idx1", def = "{'field1': 1, 'field2': -1}"),
 *         @CompoundIndex(name = "idx2", def = "{'field3': 1}", unique = true)
 *     })
 *     public class ComplexDocument { }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @DBRef
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Reference another document (like foreign key)
 * 
 * EXAMPLE:
 * ---------
 *     @Document(collection = "orders")
 *     public class Order {
 *         
 *         @Id
 *         private String id;
 *         
 *         @DBRef  // References User document
 *         private User customer;
 *         
 *         @DBRef(lazy = true)  // Lazy loading
 *         private List<Product> products;
 *         
 *         private BigDecimal total;
 *     }
 *     
 *     // Alternative: Manual reference (more flexible)
 *     @Document
 *     public class Order {
 *         
 *         private String customerId;  // Just store the ID
 *         
 *         @DocumentReference  // Spring Data 3.x+ - better than @DBRef
 *         private User customer;
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * EMBEDDED DOCUMENTS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @Document(collection = "users")
 *     public class User {
 *         
 *         @Id
 *         private String id;
 *         private String name;
 *         
 *         // Embedded document (stored inside User document)
 *         private Address address;
 *         
 *         // List of embedded documents
 *         private List<PhoneNumber> phoneNumbers;
 *     }
 *     
 *     // No @Document needed - it's embedded
 *     public class Address {
 *         private String street;
 *         private String city;
 *         private String zipCode;
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * MONGODB REPOSITORY
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     public interface UserRepository extends MongoRepository<User, String> {
 *         
 *         // Query methods (same as JPA)
 *         List<User> findByName(String name);
 *         Optional<User> findByEmail(String email);
 *         List<User> findByAgeGreaterThan(int age);
 *         
 *         // Custom query with @Query
 *         @Query("{ 'name': ?0 }")
 *         List<User> findByNameCustom(String name);
 *         
 *         @Query("{ 'age': { $gte: ?0, $lte: ?1 } }")
 *         List<User> findByAgeRange(int min, int max);
 *         
 *         // With projection
 *         @Query(value = "{ 'status': 'ACTIVE' }", fields = "{ 'name': 1, 'email': 1 }")
 *         List<User> findActiveUsersProjected();
 *         
 *         // Text search
 *         @Query("{ $text: { $search: ?0 } }")
 *         List<User> searchByText(String text);
 *         
 *         // Aggregation
 *         @Aggregation(pipeline = {
 *             "{ $match: { status: 'ACTIVE' } }",
 *             "{ $group: { _id: '$department', count: { $sum: 1 } } }"
 *         })
 *         List<DepartmentCount> countByDepartment();
 *     }
 * 
 * CONFIGURATION:
 * ---------------
 *     # application.properties
 *     spring.data.mongodb.uri=mongodb://localhost:27017/mydb
 *     # OR
 *     spring.data.mongodb.host=localhost
 *     spring.data.mongodb.port=27017
 *     spring.data.mongodb.database=mydb
 */
