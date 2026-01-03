package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 37: JPA AUDITING ANNOTATIONS                                           ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Auto-populate created/modified timestamps and user info.
 */
public class _37_JpaAuditingAnnotations {
    public static void main(String[] args) {
        System.out.println("=== JPA AUDITING ANNOTATIONS ===\n");
        System.out.println("@EnableJpaAuditing → Enable auditing");
        System.out.println("@CreatedDate       → Auto-set creation time");
        System.out.println("@LastModifiedDate  → Auto-set update time");
        System.out.println("@CreatedBy         → Auto-set creator");
        System.out.println("@LastModifiedBy    → Auto-set modifier");
        System.out.println("@EntityListeners   → Attach listener class");
        System.out.println("@Version           → Optimistic locking");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * ENABLE AUDITING
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     @EnableJpaAuditing(auditorAwareRef = "auditorProvider")
 *     public class JpaConfig {
 *         
 *         @Bean
 *         public AuditorAware<String> auditorProvider() {
 *             return () -> Optional.ofNullable(SecurityContextHolder.getContext())
 *                 .map(SecurityContext::getAuthentication)
 *                 .map(Authentication::getName);
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @CreatedDate and @LastModifiedDate
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Auto-populate timestamps
 * 
 * EXAMPLE:
 * ---------
 *     @Entity
 *     @EntityListeners(AuditingEntityListener.class)
 *     public class User {
 *         
 *         @Id @GeneratedValue
 *         private Long id;
 *         
 *         private String name;
 *         
 *         @CreatedDate
 *         @Column(updatable = false)
 *         private LocalDateTime createdAt;
 *         
 *         @LastModifiedDate
 *         private LocalDateTime updatedAt;
 *     }
 *     
 *     // Usage - timestamps auto-set!
 *     User user = new User();
 *     user.setName("John");
 *     userRepository.save(user);
 *     // createdAt = now, updatedAt = now
 *     
 *     user.setName("Jane");
 *     userRepository.save(user);
 *     // createdAt = unchanged, updatedAt = now
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @CreatedBy and @LastModifiedBy
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Auto-populate who created/modified the entity
 * 
 * EXAMPLE:
 * ---------
 *     @Entity
 *     @EntityListeners(AuditingEntityListener.class)
 *     public class Document {
 *         
 *         @Id @GeneratedValue
 *         private Long id;
 *         
 *         private String title;
 *         
 *         @CreatedBy
 *         @Column(updatable = false)
 *         private String createdBy;
 *         
 *         @LastModifiedBy
 *         private String modifiedBy;
 *         
 *         @CreatedDate
 *         @Column(updatable = false)
 *         private LocalDateTime createdAt;
 *         
 *         @LastModifiedDate
 *         private LocalDateTime updatedAt;
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * BASE ENTITY (Reusable)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Common audit fields for all entities
 * 
 * EXAMPLE:
 * ---------
 *     @MappedSuperclass
 *     @EntityListeners(AuditingEntityListener.class)
 *     public abstract class BaseEntity {
 *         
 *         @CreatedDate
 *         @Column(updatable = false)
 *         private LocalDateTime createdAt;
 *         
 *         @LastModifiedDate
 *         private LocalDateTime updatedAt;
 *         
 *         @CreatedBy
 *         @Column(updatable = false)
 *         private String createdBy;
 *         
 *         @LastModifiedBy
 *         private String updatedBy;
 *         
 *         // Getters and setters
 *     }
 *     
 *     // All entities extend BaseEntity
 *     @Entity
 *     public class User extends BaseEntity {
 *         @Id @GeneratedValue
 *         private Long id;
 *         private String name;
 *     }
 *     
 *     @Entity
 *     public class Order extends BaseEntity {
 *         @Id @GeneratedValue
 *         private Long id;
 *         private BigDecimal total;
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Version (Optimistic Locking)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Prevent concurrent modification (lost updates)
 * 
 * EXAMPLE:
 * ---------
 *     @Entity
 *     public class Product {
 *         
 *         @Id @GeneratedValue
 *         private Long id;
 *         
 *         private String name;
 *         private Integer stock;
 *         
 *         @Version
 *         private Long version;  // Auto-incremented on save
 *     }
 *     
 *     // How it works:
 *     // 1. User A reads product (version=1)
 *     // 2. User B reads product (version=1)
 *     // 3. User A saves (version becomes 2) ✓
 *     // 4. User B tries to save (expected version=1, actual=2) ✗
 *     //    → OptimisticLockException thrown!
 *     
 *     @Service
 *     public class ProductService {
 *         
 *         @Transactional
 *         public Product updateStock(Long id, int quantity) {
 *             try {
 *                 Product product = productRepository.findById(id).orElseThrow();
 *                 product.setStock(product.getStock() + quantity);
 *                 return productRepository.save(product);
 *             } catch (OptimisticLockException e) {
 *                 // Someone else modified it - retry or handle
 *                 throw new ConcurrentModificationException("Product was modified");
 *             }
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * CUSTOM ENTITY LISTENER
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     public class UserEntityListener {
 *         
 *         @PrePersist
 *         public void prePersist(User user) {
 *             user.setStatus("ACTIVE");
 *             user.setCreatedAt(LocalDateTime.now());
 *         }
 *         
 *         @PreUpdate
 *         public void preUpdate(User user) {
 *             user.setUpdatedAt(LocalDateTime.now());
 *         }
 *         
 *         @PostPersist
 *         public void postPersist(User user) {
 *             // Send welcome email, log, etc.
 *         }
 *         
 *         @PostUpdate
 *         public void postUpdate(User user) {
 *             // Log changes
 *         }
 *         
 *         @PreRemove
 *         public void preRemove(User user) {
 *             // Cleanup before delete
 *         }
 *         
 *         @PostRemove
 *         public void postRemove(User user) {
 *             // After delete
 *         }
 *         
 *         @PostLoad
 *         public void postLoad(User user) {
 *             // After entity loaded from DB
 *         }
 *     }
 *     
 *     @Entity
 *     @EntityListeners({AuditingEntityListener.class, UserEntityListener.class})
 *     public class User { }
 * 
 * JPA LIFECYCLE CALLBACKS:
 * -------------------------
 * | Annotation   | When                          |
 * |--------------|-------------------------------|
 * | @PrePersist  | Before INSERT                 |
 * | @PostPersist | After INSERT                  |
 * | @PreUpdate   | Before UPDATE                 |
 * | @PostUpdate  | After UPDATE                  |
 * | @PreRemove   | Before DELETE                 |
 * | @PostRemove  | After DELETE                  |
 * | @PostLoad    | After SELECT                  |
 */
