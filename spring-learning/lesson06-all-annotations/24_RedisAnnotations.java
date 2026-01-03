package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 24: REDIS ANNOTATIONS                                                  ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Redis is an in-memory data store used for caching, sessions, pub/sub.
 * 
 * Requires: spring-boot-starter-data-redis
 */
public class _24_RedisAnnotations {
    public static void main(String[] args) {
        System.out.println("=== REDIS ANNOTATIONS ===\n");
        System.out.println("@RedisHash       → Mark class as Redis hash");
        System.out.println("@Id              → Hash identifier");
        System.out.println("@Indexed         → Create secondary index");
        System.out.println("@TimeToLive      → Auto-expire data");
        System.out.println("@Reference       → Reference another hash");
        System.out.println("@Cacheable       → Cache method result in Redis");
        System.out.println("@EnableRedisRepositories → Enable Redis repos");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * @RedisHash
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Store objects as Redis hashes
 * 
 * EXAMPLE:
 * ---------
 *     @RedisHash("users")  // Key prefix: users
 *     public class User {
 *         
 *         @Id
 *         private String id;  // Full key: users:123
 *         
 *         private String name;
 *         private String email;
 *         
 *         @Indexed  // Enables findByEmail()
 *         private String email;
 *         
 *         @TimeToLive  // TTL in seconds
 *         private Long expiration;
 *     }
 *     
 *     // With TTL on class level
 *     @RedisHash(value = "sessions", timeToLive = 3600)  // 1 hour
 *     public class Session {
 *         @Id
 *         private String id;
 *         private String userId;
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Indexed
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Create secondary index for queries (Redis doesn't index by default)
 * 
 * EXAMPLE:
 * ---------
 *     @RedisHash("products")
 *     public class Product {
 *         
 *         @Id
 *         private String id;
 *         
 *         @Indexed  // Can query: findByCategory()
 *         private String category;
 *         
 *         @Indexed  // Can query: findByPriceLessThan()
 *         private Double price;
 *         
 *         private String description;  // NOT indexed - can't query directly
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @TimeToLive
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Auto-expire data after specified time
 * 
 * EXAMPLE:
 * ---------
 *     @RedisHash("tokens")
 *     public class Token {
 *         
 *         @Id
 *         private String id;
 *         
 *         private String userId;
 *         
 *         @TimeToLive  // Value in seconds
 *         private Long ttl = 3600L;  // Expires in 1 hour
 *     }
 *     
 *     // Dynamic TTL
 *     @RedisHash("cache")
 *     public class CacheEntry {
 *         
 *         @Id
 *         private String key;
 *         
 *         private Object value;
 *         
 *         @TimeToLive(unit = TimeUnit.MINUTES)
 *         private Long expirationMinutes;
 *         
 *         public void setExpiration(long minutes) {
 *             this.expirationMinutes = minutes;
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * REDIS REPOSITORY
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     @EnableRedisRepositories
 *     public class RedisConfig { }
 *     
 *     public interface UserRepository extends CrudRepository<User, String> {
 *         
 *         // Only works if field has @Indexed
 *         List<User> findByEmail(String email);
 *         List<User> findByCategory(String category);
 *         List<User> findByPriceLessThan(Double price);
 *     }
 *     
 *     // Usage
 *     @Service
 *     public class UserService {
 *         
 *         @Autowired
 *         private UserRepository userRepository;
 *         
 *         public User save(User user) {
 *             return userRepository.save(user);
 *         }
 *         
 *         public Optional<User> findById(String id) {
 *             return userRepository.findById(id);
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * REDIS TEMPLATE (Manual Operations)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @Service
 *     public class RedisService {
 *         
 *         @Autowired
 *         private RedisTemplate<String, Object> redisTemplate;
 *         
 *         @Autowired
 *         private StringRedisTemplate stringRedisTemplate;
 *         
 *         // String operations
 *         public void setString(String key, String value) {
 *             stringRedisTemplate.opsForValue().set(key, value);
 *         }
 *         
 *         public void setWithExpiry(String key, String value, long seconds) {
 *             stringRedisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
 *         }
 *         
 *         public String getString(String key) {
 *             return stringRedisTemplate.opsForValue().get(key);
 *         }
 *         
 *         // Hash operations
 *         public void setHash(String key, String field, Object value) {
 *             redisTemplate.opsForHash().put(key, field, value);
 *         }
 *         
 *         // List operations
 *         public void addToList(String key, String value) {
 *             redisTemplate.opsForList().rightPush(key, value);
 *         }
 *         
 *         // Set operations
 *         public void addToSet(String key, String... values) {
 *             redisTemplate.opsForSet().add(key, values);
 *         }
 *         
 *         // Sorted set operations
 *         public void addToSortedSet(String key, String value, double score) {
 *             redisTemplate.opsForZSet().add(key, value, score);
 *         }
 *         
 *         // Delete
 *         public void delete(String key) {
 *             redisTemplate.delete(key);
 *         }
 *         
 *         // Check exists
 *         public boolean exists(String key) {
 *             return Boolean.TRUE.equals(redisTemplate.hasKey(key));
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * REDIS CACHING WITH @Cacheable
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     @EnableCaching
 *     public class RedisCacheConfig {
 *         
 *         @Bean
 *         public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
 *             RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
 *                 .entryTtl(Duration.ofMinutes(10))
 *                 .serializeValuesWith(
 *                     RedisSerializationContext.SerializationPair
 *                         .fromSerializer(new GenericJackson2JsonRedisSerializer())
 *                 );
 *             
 *             return RedisCacheManager.builder(factory)
 *                 .cacheDefaults(config)
 *                 .build();
 *         }
 *     }
 *     
 *     @Service
 *     public class ProductService {
 *         
 *         @Cacheable(value = "products", key = "#id")
 *         public Product findById(String id) {
 *             // This result is cached in Redis
 *             return productRepository.findById(id).orElse(null);
 *         }
 *         
 *         @CacheEvict(value = "products", key = "#product.id")
 *         public Product update(Product product) {
 *             return productRepository.save(product);
 *         }
 *     }
 * 
 * CONFIGURATION:
 * ---------------
 *     # application.properties
 *     spring.redis.host=localhost
 *     spring.redis.port=6379
 *     spring.redis.password=
 *     spring.redis.database=0
 *     
 *     # Connection pool
 *     spring.redis.lettuce.pool.max-active=8
 *     spring.redis.lettuce.pool.max-idle=8
 * 
 * REDIS DATA TYPES:
 * ------------------
 * | Type       | Use Case                    | Operations       |
 * |------------|-----------------------------| -----------------|
 * | String     | Simple key-value            | opsForValue()    |
 * | Hash       | Objects with fields         | opsForHash()     |
 * | List       | Ordered collection          | opsForList()     |
 * | Set        | Unique values               | opsForSet()      |
 * | Sorted Set | Ranked/scored values        | opsForZSet()     |
 */
