package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 4: WEB / REST ANNOTATIONS                                              ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 */
public class _04_WebRestAnnotations {
    public static void main(String[] args) {
        System.out.println("=== WEB / REST ANNOTATIONS ===\n");
        System.out.println("@RequestMapping  → Map URL to handler");
        System.out.println("@GetMapping      → Handle GET requests");
        System.out.println("@PostMapping     → Handle POST requests");
        System.out.println("@PutMapping      → Handle PUT requests");
        System.out.println("@DeleteMapping   → Handle DELETE requests");
        System.out.println("@PathVariable    → Extract from URL path /users/{id}");
        System.out.println("@RequestParam    → Extract query params ?name=john");
        System.out.println("@RequestBody     → Convert JSON body to object");
        System.out.println("@ResponseStatus  → Set HTTP status code");
        System.out.println("@RequestHeader   → Extract HTTP headers");
        System.out.println("@CrossOrigin     → Enable CORS");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * @RequestMapping
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Maps HTTP requests to handler methods
 * 
 * WHERE: Class level (base path) or method level
 * 
 * EXAMPLE:
 * ---------
 *     @RestController
 *     @RequestMapping("/api/v1/products")  // Base path
 *     public class ProductController {
 *         
 *         @RequestMapping(method = RequestMethod.GET)
 *         public List<Product> getAll() { }
 *         
 *         @RequestMapping(
 *             method = RequestMethod.POST,
 *             consumes = MediaType.APPLICATION_JSON_VALUE,
 *             produces = MediaType.APPLICATION_JSON_VALUE
 *         )
 *         public Product create(@RequestBody Product product) { }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @GetMapping, @PostMapping, @PutMapping, @DeleteMapping, @PatchMapping
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Shortcuts for @RequestMapping with specific HTTP methods
 * 
 * WHERE: Handler methods in controllers
 * 
 * EXAMPLE:
 * ---------
 *     @RestController
 *     @RequestMapping("/api/users")
 *     public class UserController {
 *         
 *         // GET /api/users
 *         @GetMapping
 *         public List<User> getAllUsers() {
 *             return userService.findAll();
 *         }
 *         
 *         // GET /api/users/123
 *         @GetMapping("/{id}")
 *         public User getUser(@PathVariable Long id) {
 *             return userService.findById(id);
 *         }
 *         
 *         // POST /api/users
 *         @PostMapping
 *         @ResponseStatus(HttpStatus.CREATED)
 *         public User createUser(@RequestBody @Valid User user) {
 *             return userService.save(user);
 *         }
 *         
 *         // PUT /api/users/123
 *         @PutMapping("/{id}")
 *         public User updateUser(@PathVariable Long id, @RequestBody User user) {
 *             return userService.update(id, user);
 *         }
 *         
 *         // DELETE /api/users/123
 *         @DeleteMapping("/{id}")
 *         @ResponseStatus(HttpStatus.NO_CONTENT)
 *         public void deleteUser(@PathVariable Long id) {
 *             userService.deleteById(id);
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @PathVariable
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Extracts values from the URL path
 * 
 * WHERE: Method parameters
 * 
 * EXAMPLE:
 * ---------
 *     // GET /api/users/123
 *     @GetMapping("/users/{id}")
 *     public User getUser(@PathVariable Long id) { }
 *     
 *     // GET /api/users/123/orders/456
 *     @GetMapping("/users/{userId}/orders/{orderId}")
 *     public Order getOrder(
 *         @PathVariable Long userId,
 *         @PathVariable Long orderId
 *     ) { }
 *     
 *     // Different variable name
 *     @GetMapping("/products/{product-id}")
 *     public Product getProduct(@PathVariable("product-id") Long productId) { }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @RequestParam
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Extracts query parameters from the URL (?key=value)
 * 
 * WHERE: Method parameters
 * 
 * EXAMPLE:
 * ---------
 *     // GET /api/products?category=electronics
 *     @GetMapping("/products")
 *     public List<Product> getByCategory(@RequestParam String category) { }
 *     
 *     // GET /api/products/search?name=phone&minPrice=100&maxPrice=1000
 *     @GetMapping("/products/search")
 *     public List<Product> search(
 *         @RequestParam String name,
 *         @RequestParam(required = false) Double minPrice,
 *         @RequestParam(defaultValue = "10000") Double maxPrice
 *     ) { }
 *     
 *     // GET /api/products?tags=new&tags=sale
 *     @GetMapping("/products/filter")
 *     public List<Product> filterByTags(@RequestParam List<String> tags) { }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @RequestBody
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Converts the HTTP request body (JSON) to a Java object
 * 
 * WHERE: Method parameters for POST/PUT/PATCH requests
 * 
 * EXAMPLE:
 * ---------
 *     // POST /api/orders
 *     // Body: {"customerId": 123, "items": [...]}
 *     @PostMapping("/orders")
 *     public Order createOrder(@RequestBody OrderRequest request) { }
 *     
 *     // With validation
 *     @PostMapping("/orders")
 *     public Order create(@RequestBody @Valid OrderRequest request) { }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @ResponseStatus
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Sets the HTTP status code for the response
 * 
 * WHERE: On handler methods or exception classes
 * 
 * EXAMPLE:
 * ---------
 *     @PostMapping
 *     @ResponseStatus(HttpStatus.CREATED)  // 201
 *     public User create(@RequestBody User user) { }
 *     
 *     @DeleteMapping("/{id}")
 *     @ResponseStatus(HttpStatus.NO_CONTENT)  // 204
 *     public void delete(@PathVariable Long id) { }
 *     
 *     // On exception class:
 *     @ResponseStatus(HttpStatus.NOT_FOUND)  // 404
 *     public class ResourceNotFoundException extends RuntimeException { }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @RequestHeader
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Extracts values from HTTP request headers
 * 
 * WHERE: Method parameters
 * 
 * EXAMPLE:
 * ---------
 *     @GetMapping("/info")
 *     public String getInfo(
 *         @RequestHeader("User-Agent") String userAgent,
 *         @RequestHeader("Authorization") String authToken,
 *         @RequestHeader(value = "X-Custom", defaultValue = "none") String custom
 *     ) { }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @CrossOrigin
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Enables Cross-Origin Resource Sharing (CORS)
 * 
 * WHERE: On controller class or methods
 * 
 * EXAMPLE:
 * ---------
 *     @RestController
 *     @CrossOrigin(origins = "http://localhost:3000")
 *     @RequestMapping("/api")
 *     public class ApiController { }
 *     
 *     // Multiple origins
 *     @CrossOrigin(origins = {"http://localhost:3000", "https://myapp.com"})
 */
