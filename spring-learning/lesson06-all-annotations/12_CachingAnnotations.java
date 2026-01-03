package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 12: CACHING ANNOTATIONS                                                ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 */
public class _12_CachingAnnotations {
    public static void main(String[] args) {
        System.out.println("=== CACHING ANNOTATIONS ===\n");
        System.out.println("@EnableCaching → Enable Spring caching");
        System.out.println("@Cacheable     → Cache method result");
        System.out.println("@CachePut      → Update cache");
        System.out.println("@CacheEvict    → Remove from cache");
        System.out.println("@Caching       → Combine multiple cache operations");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * CACHING ANNOTATIONS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Cache method results to avoid repeated expensive operations
 * 
 * EXAMPLE:
 * ---------
 *     @Configuration
 *     @EnableCaching  // Required to enable caching
 *     public class CacheConfig { }
 *     
 *     @Service
 *     public class UserService {
 *         
 *         // Cache the result - next call with same id returns cached value
 *         @Cacheable(value = "users", key = "#id")
 *         public User findById(Long id) {
 *             System.out.println("Fetching from database...");
 *             return userRepository.findById(id).orElse(null);
 *         }
 *         
 *         // Conditional caching
 *         @Cacheable(value = "users", key = "#id", condition = "#id > 10")
 *         public User findByIdConditional(Long id) {
 *             return userRepository.findById(id).orElse(null);
 *         }
 *         
 *         // Don't cache null results
 *         @Cacheable(value = "users", key = "#id", unless = "#result == null")
 *         public User findByIdNotNull(Long id) {
 *             return userRepository.findById(id).orElse(null);
 *         }
 *         
 *         // Update cache after save
 *         @CachePut(value = "users", key = "#user.id")
 *         public User save(User user) {
 *             return userRepository.save(user);
 *         }
 *         
 *         // Remove from cache after delete
 *         @CacheEvict(value = "users", key = "#id")
 *         public void delete(Long id) {
 *             userRepository.deleteById(id);
 *         }
 *         
 *         // Clear entire cache
 *         @CacheEvict(value = "users", allEntries = true)
 *         public void clearCache() {
 *             // Cache is cleared
 *         }
 *         
 *         // Multiple cache operations
 *         @Caching(
 *             cacheable = @Cacheable(value = "users", key = "#email"),
 *             put = @CachePut(value = "userEmails", key = "#result.id")
 *         )
 *         public User findByEmail(String email) {
 *             return userRepository.findByEmail(email).orElse(null);
 *         }
 *     }
 * 
 * CACHE PROVIDERS:
 * -----------------
 * Spring Boot auto-configures cache based on classpath:
 * - Simple (ConcurrentHashMap) - default
 * - Caffeine - high-performance in-memory
 * - Redis - distributed cache
 * - EhCache - popular Java cache
 * - Hazelcast - distributed cache
 * 
 * EXAMPLE - Caffeine Configuration:
 * ----------------------------------
 *     # application.properties
 *     spring.cache.type=caffeine
 *     spring.cache.caffeine.spec=maximumSize=500,expireAfterAccess=600s
 * 
 * EXAMPLE - Redis Configuration:
 * -------------------------------
 *     # application.properties
 *     spring.cache.type=redis
 *     spring.redis.host=localhost
 *     spring.redis.port=6379
 */
