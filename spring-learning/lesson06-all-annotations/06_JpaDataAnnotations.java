package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 6: JPA / DATA ANNOTATIONS                                              ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Requires: spring-boot-starter-data-jpa
 */
public class _06_JpaDataAnnotations {
    public static void main(String[] args) {
        System.out.println("=== JPA / DATA ANNOTATIONS ===\n");
        System.out.println("@Entity        → Mark class as database table");
        System.out.println("@Table         → Customize table name");
        System.out.println("@Id            → Primary key field");
        System.out.println("@GeneratedValue→ Auto-generate ID");
        System.out.println("@Column        → Customize column");
        System.out.println("@OneToMany     → One-to-many relationship");
        System.out.println("@ManyToOne     → Many-to-one relationship");
        System.out.println("@ManyToMany    → Many-to-many relationship");
        System.out.println("@Transactional → Database transaction");
        System.out.println("@Query         → Custom JPQL/SQL query");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * ENTITY ANNOTATIONS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @Entity
 *     @Table(name = "users")
 *     public class User {
 *         
 *         @Id
 *         @GeneratedValue(strategy = GenerationType.IDENTITY)
 *         private Long id;
 *         
 *         @Column(name = "full_name", nullable = false, length = 100)
 *         private String name;
 *         
 *         @Column(unique = true)
 *         private String email;
 *         
 *         @Enumerated(EnumType.STRING)  // Store enum as string
 *         private UserStatus status;
 *         
 *         @CreatedDate  // Auto-set on creation
 *         private LocalDateTime createdDate;
 *         
 *         @LastModifiedDate  // Auto-update on modification
 *         private LocalDateTime updatedDate;
 *         
 *         @Version  // Optimistic locking
 *         private Long version;
 *         
 *         @Lob  // Large object (BLOB/CLOB)
 *         private byte[] photo;
 *         
 *         @Transient  // Not persisted to database
 *         private String tempData;
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * RELATIONSHIP ANNOTATIONS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE - ONE TO MANY:
 * -----------------------
 *     @Entity
 *     public class User {
 *         @Id
 *         @GeneratedValue(strategy = GenerationType.IDENTITY)
 *         private Long id;
 *         
 *         // One user has many orders
 *         @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
 *         private List<Order> orders = new ArrayList<>();
 *     }
 *     
 *     @Entity
 *     public class Order {
 *         @Id
 *         @GeneratedValue(strategy = GenerationType.IDENTITY)
 *         private Long id;
 *         
 *         // Many orders belong to one user
 *         @ManyToOne(fetch = FetchType.LAZY)
 *         @JoinColumn(name = "user_id", nullable = false)
 *         private User user;
 *     }
 * 
 * EXAMPLE - ONE TO ONE:
 * ----------------------
 *     @Entity
 *     public class User {
 *         @OneToOne(cascade = CascadeType.ALL)
 *         @JoinColumn(name = "profile_id")
 *         private Profile profile;
 *     }
 * 
 * EXAMPLE - MANY TO MANY:
 * ------------------------
 *     @Entity
 *     public class User {
 *         @ManyToMany
 *         @JoinTable(
 *             name = "user_roles",
 *             joinColumns = @JoinColumn(name = "user_id"),
 *             inverseJoinColumns = @JoinColumn(name = "role_id")
 *         )
 *         private Set<Role> roles = new HashSet<>();
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Transactional
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Wraps method in a database transaction
 * 
 * WHERE: Service methods that modify data
 * 
 * EXAMPLE:
 * ---------
 *     @Service
 *     public class OrderService {
 *         
 *         @Transactional
 *         public Order createOrder(OrderRequest request) {
 *             Order order = new Order();
 *             orderRepository.save(order);
 *             paymentService.process(order);  // Same transaction
 *             return order;
 *         }
 *         
 *         @Transactional(readOnly = true)  // Performance optimization
 *         public List<Order> findAll() {
 *             return orderRepository.findAll();
 *         }
 *         
 *         @Transactional(rollbackFor = Exception.class)
 *         public void riskyOperation() { }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Query and @Modifying
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Define custom queries in repositories
 * 
 * EXAMPLE:
 * ---------
 *     @Repository
 *     public interface UserRepository extends JpaRepository<User, Long> {
 *         
 *         // JPQL query
 *         @Query("SELECT u FROM User u WHERE u.status = :status")
 *         List<User> findByStatus(@Param("status") UserStatus status);
 *         
 *         // Native SQL
 *         @Query(value = "SELECT * FROM users WHERE email LIKE %:domain", 
 *                nativeQuery = true)
 *         List<User> findByEmailDomain(@Param("domain") String domain);
 *         
 *         // Update query (requires @Modifying)
 *         @Modifying
 *         @Query("UPDATE User u SET u.status = :status WHERE u.id = :id")
 *         int updateStatus(@Param("id") Long id, @Param("status") String status);
 *         
 *         // Delete query
 *         @Modifying
 *         @Query("DELETE FROM User u WHERE u.lastLogin < :date")
 *         int deleteInactiveUsers(@Param("date") LocalDateTime date);
 *     }
 * 
 * FETCH TYPES:
 * -------------
 * | Type  | Meaning                    | Default For     |
 * |-------|----------------------------|-----------------|
 * | LAZY  | Load when accessed         | @OneToMany      |
 * | EAGER | Load immediately with parent| @ManyToOne     |
 * 
 * CASCADE TYPES:
 * ---------------
 * | Type    | Meaning                                |
 * |---------|----------------------------------------|
 * | ALL     | All operations cascade                 |
 * | PERSIST | Save cascades                          |
 * | MERGE   | Update cascades                        |
 * | REMOVE  | Delete cascades                        |
 * | REFRESH | Refresh cascades                       |
 */
