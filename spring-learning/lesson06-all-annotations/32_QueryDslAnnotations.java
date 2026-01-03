package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 32: QUERYDSL ANNOTATIONS                                               ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * QueryDSL provides type-safe queries instead of string-based JPQL/SQL.
 * 
 * Requires: querydsl-jpa + querydsl-apt (annotation processor)
 */
public class _32_QueryDslAnnotations {
    public static void main(String[] args) {
        System.out.println("=== QUERYDSL ===\n");
        System.out.println("@QueryEntity     → Generate Q-class for entity");
        System.out.println("@QueryProjection → Type-safe DTO projection");
        System.out.println("@QueryInit       → Initialize nested paths");
        System.out.println("@QueryTransient  → Exclude from Q-class");
        System.out.println("QuerydslPredicateExecutor → Repository support");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * WHY QUERYDSL?
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PROBLEM - String-based queries are error-prone:
 *     @Query("SELECT u FROM User u WHERE u.naem = :name")  // Typo! Runtime error!
 * 
 * SOLUTION - QueryDSL is compile-time safe:
 *     QUser.user.name.eq(name)  // Compile error if 'name' doesn't exist!
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * SETUP
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * 1. Add dependencies
 * 2. Run compile - generates Q-classes (QUser, QOrder, etc.)
 * 3. Use Q-classes for type-safe queries
 * 
 * GENERATED CLASS EXAMPLE:
 * -------------------------
 *     // Entity
 *     @Entity
 *     public class User {
 *         private Long id;
 *         private String name;
 *         private String email;
 *     }
 *     
 *     // Generated Q-class (auto-generated, don't edit!)
 *     public class QUser extends EntityPathBase<User> {
 *         public static final QUser user = new QUser("user");
 *         public final NumberPath<Long> id = createNumber("id", Long.class);
 *         public final StringPath name = createString("name");
 *         public final StringPath email = createString("email");
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * QuerydslPredicateExecutor
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Add QueryDSL support to Spring Data repositories
 * 
 * EXAMPLE:
 * ---------
 *     public interface UserRepository extends 
 *             JpaRepository<User, Long>,
 *             QuerydslPredicateExecutor<User> {
 *         // Now supports Predicate-based queries!
 *     }
 *     
 *     // Usage in service
 *     @Service
 *     public class UserService {
 *         
 *         @Autowired
 *         private UserRepository userRepository;
 *         
 *         public List<User> findByName(String name) {
 *             QUser user = QUser.user;
 *             return (List<User>) userRepository.findAll(
 *                 user.name.eq(name)
 *             );
 *         }
 *         
 *         public List<User> search(String keyword) {
 *             QUser user = QUser.user;
 *             return (List<User>) userRepository.findAll(
 *                 user.name.containsIgnoreCase(keyword)
 *                     .or(user.email.containsIgnoreCase(keyword))
 *             );
 *         }
 *         
 *         public Page<User> searchPaged(String keyword, Pageable pageable) {
 *             QUser user = QUser.user;
 *             return userRepository.findAll(
 *                 user.name.containsIgnoreCase(keyword),
 *                 pageable
 *             );
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * BUILDING PREDICATES
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     QUser user = QUser.user;
 *     
 *     // Equality
 *     user.name.eq("John")
 *     user.name.ne("John")           // not equal
 *     user.name.isNull()
 *     user.name.isNotNull()
 *     
 *     // String operations
 *     user.name.contains("oh")       // LIKE '%oh%'
 *     user.name.startsWith("Jo")     // LIKE 'Jo%'
 *     user.name.endsWith("hn")       // LIKE '%hn'
 *     user.name.containsIgnoreCase("john")
 *     user.name.matches("J.*")       // Regex
 *     
 *     // Numeric comparisons
 *     user.age.gt(18)                // >
 *     user.age.goe(18)               // >=
 *     user.age.lt(65)                // <
 *     user.age.loe(65)               // <=
 *     user.age.between(18, 65)
 *     
 *     // Collections
 *     user.roles.contains("ADMIN")
 *     user.roles.isEmpty()
 *     user.roles.isNotEmpty()
 *     user.id.in(Arrays.asList(1L, 2L, 3L))
 *     
 *     // Combining predicates
 *     user.name.eq("John").and(user.age.gt(18))
 *     user.name.eq("John").or(user.name.eq("Jane"))
 *     user.name.eq("John").not()
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * DYNAMIC QUERY BUILDING
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @Service
 *     public class UserSearchService {
 *         
 *         public List<User> search(UserSearchCriteria criteria) {
 *             QUser user = QUser.user;
 *             BooleanBuilder builder = new BooleanBuilder();
 *             
 *             // Add conditions dynamically
 *             if (criteria.getName() != null) {
 *                 builder.and(user.name.containsIgnoreCase(criteria.getName()));
 *             }
 *             
 *             if (criteria.getEmail() != null) {
 *                 builder.and(user.email.eq(criteria.getEmail()));
 *             }
 *             
 *             if (criteria.getMinAge() != null) {
 *                 builder.and(user.age.goe(criteria.getMinAge()));
 *             }
 *             
 *             if (criteria.getMaxAge() != null) {
 *                 builder.and(user.age.loe(criteria.getMaxAge()));
 *             }
 *             
 *             if (criteria.getStatuses() != null && !criteria.getStatuses().isEmpty()) {
 *                 builder.and(user.status.in(criteria.getStatuses()));
 *             }
 *             
 *             return (List<User>) userRepository.findAll(builder);
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @QueryProjection (DTO Projection)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Type-safe DTO projections in queries
 * 
 * EXAMPLE:
 * ---------
 *     public class UserDTO {
 *         private String name;
 *         private String email;
 *         
 *         @QueryProjection  // Generates QUserDTO
 *         public UserDTO(String name, String email) {
 *             this.name = name;
 *             this.email = email;
 *         }
 *     }
 *     
 *     // Usage with JPAQueryFactory
 *     @Service
 *     public class UserService {
 *         
 *         @Autowired
 *         private JPAQueryFactory queryFactory;
 *         
 *         public List<UserDTO> getUserDTOs() {
 *             QUser user = QUser.user;
 *             
 *             return queryFactory
 *                 .select(new QUserDTO(user.name, user.email))  // Type-safe!
 *                 .from(user)
 *                 .fetch();
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * JPAQueryFactory (Complex Queries)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     public class QueryDslConfig {
 *         
 *         @Bean
 *         public JPAQueryFactory jpaQueryFactory(EntityManager em) {
 *             return new JPAQueryFactory(em);
 *         }
 *     }
 *     
 *     @Service
 *     public class OrderService {
 *         
 *         @Autowired
 *         private JPAQueryFactory queryFactory;
 *         
 *         // Join query
 *         public List<Order> findOrdersWithUser(String userName) {
 *             QOrder order = QOrder.order;
 *             QUser user = QUser.user;
 *             
 *             return queryFactory
 *                 .selectFrom(order)
 *                 .join(order.user, user)
 *                 .where(user.name.eq(userName))
 *                 .orderBy(order.createdAt.desc())
 *                 .fetch();
 *         }
 *         
 *         // Subquery
 *         public List<User> findUsersWithOrders() {
 *             QUser user = QUser.user;
 *             QOrder order = QOrder.order;
 *             
 *             return queryFactory
 *                 .selectFrom(user)
 *                 .where(user.id.in(
 *                     JPAExpressions
 *                         .select(order.user.id)
 *                         .from(order)
 *                 ))
 *                 .fetch();
 *         }
 *         
 *         // Aggregation
 *         public List<Tuple> getOrderCountByUser() {
 *             QOrder order = QOrder.order;
 *             
 *             return queryFactory
 *                 .select(order.user.name, order.count())
 *                 .from(order)
 *                 .groupBy(order.user)
 *                 .fetch();
 *         }
 *     }
 * 
 * DEPENDENCY:
 * ------------
 *     <dependency>
 *         <groupId>com.querydsl</groupId>
 *         <artifactId>querydsl-jpa</artifactId>
 *         <classifier>jakarta</classifier>
 *     </dependency>
 */
