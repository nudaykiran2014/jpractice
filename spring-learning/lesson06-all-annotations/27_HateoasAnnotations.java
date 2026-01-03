package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 27: SPRING HATEOAS ANNOTATIONS (Hypermedia APIs)                       ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * HATEOAS = Hypermedia As The Engine Of Application State
 * APIs include links to related resources and available actions.
 * 
 * Requires: spring-boot-starter-hateoas
 */
public class _27_HateoasAnnotations {
    public static void main(String[] args) {
        System.out.println("=== SPRING HATEOAS ===\n");
        System.out.println("@EnableHypermediaSupport → Enable HATEOAS");
        System.out.println("@Relation         → Customize link relation name");
        System.out.println("RepresentationModel → Base class for resources");
        System.out.println("EntityModel       → Wrap single entity with links");
        System.out.println("CollectionModel   → Wrap collection with links");
        System.out.println("PagedModel        → Wrap paginated data with links");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * BASIC HATEOAS RESPONSE
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * Without HATEOAS:
 *     { "id": 1, "name": "John" }
 * 
 * With HATEOAS:
 *     {
 *       "id": 1,
 *       "name": "John",
 *       "_links": {
 *         "self": { "href": "/users/1" },
 *         "orders": { "href": "/users/1/orders" },
 *         "delete": { "href": "/users/1" }
 *       }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * EntityModel (Single Resource)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @RestController
 *     @RequestMapping("/api/users")
 *     public class UserController {
 *         
 *         @GetMapping("/{id}")
 *         public EntityModel<User> getUser(@PathVariable Long id) {
 *             User user = userService.findById(id);
 *             
 *             return EntityModel.of(user,
 *                 // Self link
 *                 linkTo(methodOn(UserController.class).getUser(id)).withSelfRel(),
 *                 // Link to all users
 *                 linkTo(methodOn(UserController.class).getAllUsers()).withRel("users"),
 *                 // Link to user's orders
 *                 linkTo(methodOn(OrderController.class).getOrdersByUser(id)).withRel("orders")
 *             );
 *         }
 *         
 *         // Response:
 *         // {
 *         //   "id": 1, "name": "John", "email": "john@example.com",
 *         //   "_links": {
 *         //     "self": { "href": "http://localhost:8080/api/users/1" },
 *         //     "users": { "href": "http://localhost:8080/api/users" },
 *         //     "orders": { "href": "http://localhost:8080/api/orders?userId=1" }
 *         //   }
 *         // }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * CollectionModel (List of Resources)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @GetMapping
 *     public CollectionModel<EntityModel<User>> getAllUsers() {
 *         List<EntityModel<User>> users = userService.findAll().stream()
 *             .map(user -> EntityModel.of(user,
 *                 linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel()
 *             ))
 *             .collect(Collectors.toList());
 *         
 *         return CollectionModel.of(users,
 *             linkTo(methodOn(UserController.class).getAllUsers()).withSelfRel()
 *         );
 *     }
 *     
 *     // Response:
 *     // {
 *     //   "_embedded": {
 *     //     "userList": [
 *     //       { "id": 1, "name": "John", "_links": { "self": {...} } },
 *     //       { "id": 2, "name": "Jane", "_links": { "self": {...} } }
 *     //     ]
 *     //   },
 *     //   "_links": {
 *     //     "self": { "href": "http://localhost:8080/api/users" }
 *     //   }
 *     // }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * PagedModel (Paginated Resources)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @GetMapping
 *     public PagedModel<EntityModel<User>> getUsers(Pageable pageable) {
 *         Page<User> userPage = userService.findAll(pageable);
 *         
 *         return pagedResourcesAssembler.toModel(userPage, user ->
 *             EntityModel.of(user,
 *                 linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel()
 *             )
 *         );
 *     }
 *     
 *     // Response includes pagination links:
 *     // {
 *     //   "_embedded": { "userList": [...] },
 *     //   "_links": {
 *     //     "first": { "href": "...?page=0&size=20" },
 *     //     "self": { "href": "...?page=1&size=20" },
 *     //     "next": { "href": "...?page=2&size=20" },
 *     //     "last": { "href": "...?page=5&size=20" }
 *     //   },
 *     //   "page": { "size": 20, "totalElements": 100, "totalPages": 5, "number": 1 }
 *     // }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * RepresentationModelAssembler (Reusable)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Reusable component to convert entities to models
 * 
 * EXAMPLE:
 * ---------
 *     @Component
 *     public class UserModelAssembler 
 *             implements RepresentationModelAssembler<User, EntityModel<User>> {
 *         
 *         @Override
 *         public EntityModel<User> toModel(User user) {
 *             return EntityModel.of(user,
 *                 linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel(),
 *                 linkTo(methodOn(UserController.class).getAllUsers()).withRel("users"),
 *                 linkTo(methodOn(OrderController.class).getOrdersByUser(user.getId())).withRel("orders")
 *             );
 *         }
 *     }
 *     
 *     // Usage in controller:
 *     @RestController
 *     public class UserController {
 *         
 *         @Autowired
 *         private UserModelAssembler assembler;
 *         
 *         @GetMapping("/{id}")
 *         public EntityModel<User> getUser(@PathVariable Long id) {
 *             return assembler.toModel(userService.findById(id));
 *         }
 *         
 *         @GetMapping
 *         public CollectionModel<EntityModel<User>> getAllUsers() {
 *             return assembler.toCollectionModel(userService.findAll());
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Relation
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Customize the relation name in _embedded
 * 
 * EXAMPLE:
 * ---------
 *     @Relation(collectionRelation = "users", itemRelation = "user")
 *     public class User {
 *         private Long id;
 *         private String name;
 *     }
 *     
 *     // Now _embedded will use "users" instead of "userList":
 *     // { "_embedded": { "users": [...] } }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * Affordances (Available Actions)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Tell clients what HTTP methods are available
 * 
 * EXAMPLE:
 * ---------
 *     @GetMapping("/{id}")
 *     public EntityModel<User> getUser(@PathVariable Long id) {
 *         User user = userService.findById(id);
 *         
 *         Link selfLink = linkTo(methodOn(UserController.class).getUser(id))
 *             .withSelfRel()
 *             .andAffordance(afford(methodOn(UserController.class).updateUser(id, null)))
 *             .andAffordance(afford(methodOn(UserController.class).deleteUser(id)));
 *         
 *         return EntityModel.of(user, selfLink);
 *     }
 * 
 * WHY HATEOAS?
 * -------------
 * | Benefit              | Description                              |
 * |----------------------|------------------------------------------|
 * | Discoverability      | Clients explore API via links            |
 * | Decoupling           | Clients don't hardcode URLs              |
 * | Self-documenting     | Response tells you what's possible       |
 * | Evolvability         | Change URLs without breaking clients     |
 */
