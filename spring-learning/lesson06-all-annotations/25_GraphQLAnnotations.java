package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 25: GRAPHQL ANNOTATIONS                                                ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * GraphQL is a query language for APIs - clients request exactly what they need.
 * 
 * Requires: spring-boot-starter-graphql
 */
public class _25_GraphQLAnnotations {
    public static void main(String[] args) {
        System.out.println("=== GRAPHQL ANNOTATIONS ===\n");
        System.out.println("@QueryMapping      → Handle GraphQL query");
        System.out.println("@MutationMapping   → Handle GraphQL mutation");
        System.out.println("@SubscriptionMapping → Handle subscriptions");
        System.out.println("@SchemaMapping     → Field resolver");
        System.out.println("@Argument          → Query argument");
        System.out.println("@BatchMapping      → Batch load (N+1 problem)");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * GRAPHQL SCHEMA (src/main/resources/graphql/schema.graphqls)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     type Query {
 *         users: [User]
 *         user(id: ID!): User
 *         searchUsers(name: String): [User]
 *     }
 *     
 *     type Mutation {
 *         createUser(input: CreateUserInput!): User
 *         updateUser(id: ID!, input: UpdateUserInput!): User
 *         deleteUser(id: ID!): Boolean
 *     }
 *     
 *     type Subscription {
 *         userCreated: User
 *     }
 *     
 *     type User {
 *         id: ID!
 *         name: String!
 *         email: String!
 *         posts: [Post]
 *     }
 *     
 *     type Post {
 *         id: ID!
 *         title: String!
 *         author: User!
 *     }
 *     
 *     input CreateUserInput {
 *         name: String!
 *         email: String!
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @QueryMapping
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Handle GraphQL queries (read operations)
 * 
 * EXAMPLE:
 * ---------
 *     @Controller
 *     public class UserController {
 *         
 *         @Autowired
 *         private UserService userService;
 *         
 *         // Query: { users { id name email } }
 *         @QueryMapping
 *         public List<User> users() {
 *             return userService.findAll();
 *         }
 *         
 *         // Query: { user(id: "123") { id name } }
 *         @QueryMapping
 *         public User user(@Argument String id) {
 *             return userService.findById(id);
 *         }
 *         
 *         // Query: { searchUsers(name: "John") { id name } }
 *         @QueryMapping
 *         public List<User> searchUsers(@Argument String name) {
 *             return userService.searchByName(name);
 *         }
 *         
 *         // With multiple arguments
 *         @QueryMapping
 *         public List<User> findUsers(
 *             @Argument String name,
 *             @Argument Integer age,
 *             @Argument String status
 *         ) {
 *             return userService.find(name, age, status);
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @MutationMapping
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Handle GraphQL mutations (write operations)
 * 
 * EXAMPLE:
 * ---------
 *     @Controller
 *     public class UserMutationController {
 *         
 *         // Mutation: mutation { createUser(input: {name: "John", email: "..."}) { id } }
 *         @MutationMapping
 *         public User createUser(@Argument CreateUserInput input) {
 *             return userService.create(input);
 *         }
 *         
 *         @MutationMapping
 *         public User updateUser(
 *             @Argument String id,
 *             @Argument UpdateUserInput input
 *         ) {
 *             return userService.update(id, input);
 *         }
 *         
 *         @MutationMapping
 *         public Boolean deleteUser(@Argument String id) {
 *             userService.delete(id);
 *             return true;
 *         }
 *     }
 *     
 *     // Input type
 *     public record CreateUserInput(String name, String email) {}
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @SchemaMapping (Field Resolvers)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Resolve nested fields (like user.posts)
 * 
 * EXAMPLE:
 * ---------
 *     @Controller
 *     public class UserFieldResolver {
 *         
 *         // When querying: { user(id: "1") { posts { title } } }
 *         // This resolves the "posts" field on User type
 *         @SchemaMapping(typeName = "User", field = "posts")
 *         public List<Post> getPosts(User user) {
 *             return postService.findByUserId(user.getId());
 *         }
 *         
 *         // Shorthand - method name = field name
 *         @SchemaMapping(typeName = "User")
 *         public List<Post> posts(User user) {
 *             return postService.findByUserId(user.getId());
 *         }
 *         
 *         // Computed field
 *         @SchemaMapping(typeName = "User")
 *         public String fullName(User user) {
 *             return user.getFirstName() + " " + user.getLastName();
 *         }
 *     }
 *     
 *     @Controller
 *     public class PostFieldResolver {
 *         
 *         // Resolve author field on Post
 *         @SchemaMapping(typeName = "Post")
 *         public User author(Post post) {
 *             return userService.findById(post.getAuthorId());
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @BatchMapping (Solving N+1 Problem)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Load related data in batches instead of one-by-one
 * 
 * EXAMPLE:
 * ---------
 *     @Controller
 *     public class BatchController {
 *         
 *         // Without batch: 1 query for users + N queries for posts (N+1 problem!)
 *         // With batch: 1 query for users + 1 query for ALL posts
 *         
 *         @BatchMapping(typeName = "User")
 *         public Map<User, List<Post>> posts(List<User> users) {
 *             // Get all user IDs
 *             List<String> userIds = users.stream()
 *                 .map(User::getId)
 *                 .toList();
 *             
 *             // Single query for all posts
 *             List<Post> allPosts = postService.findByUserIds(userIds);
 *             
 *             // Group by user
 *             return users.stream()
 *                 .collect(Collectors.toMap(
 *                     user -> user,
 *                     user -> allPosts.stream()
 *                         .filter(post -> post.getAuthorId().equals(user.getId()))
 *                         .toList()
 *                 ));
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @SubscriptionMapping
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Real-time updates via WebSocket
 * 
 * EXAMPLE:
 * ---------
 *     @Controller
 *     public class SubscriptionController {
 *         
 *         @SubscriptionMapping
 *         public Flux<User> userCreated() {
 *             return userEventPublisher.getUserCreatedEvents();
 *         }
 *         
 *         @SubscriptionMapping
 *         public Flux<Message> messageAdded(@Argument String roomId) {
 *             return messageService.getMessagesForRoom(roomId);
 *         }
 *     }
 * 
 * GRAPHQL CLIENT QUERY EXAMPLES:
 * -------------------------------
 *     # Query
 *     query {
 *         user(id: "123") {
 *             id
 *             name
 *             posts {
 *                 title
 *             }
 *         }
 *     }
 *     
 *     # Mutation
 *     mutation {
 *         createUser(input: { name: "John", email: "john@example.com" }) {
 *             id
 *             name
 *         }
 *     }
 *     
 *     # Subscription
 *     subscription {
 *         userCreated {
 *             id
 *             name
 *         }
 *     }
 * 
 * CONFIGURATION:
 * ---------------
 *     # application.properties
 *     spring.graphql.graphiql.enabled=true  # GraphiQL UI at /graphiql
 *     spring.graphql.schema.locations=classpath:graphql/
 */
