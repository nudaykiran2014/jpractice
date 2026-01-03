package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 10: TESTING ANNOTATIONS                                                ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Requires: spring-boot-starter-test
 */
public class _10_TestingAnnotations {
    public static void main(String[] args) {
        System.out.println("=== TESTING ANNOTATIONS ===\n");
        System.out.println("@SpringBootTest  → Full integration test");
        System.out.println("@WebMvcTest      → Test web layer only");
        System.out.println("@DataJpaTest     → Test JPA layer only");
        System.out.println("@MockBean        → Create mock bean");
        System.out.println("@SpyBean         → Partial mock");
        System.out.println("@ActiveProfiles  → Test with specific profile");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * @SpringBootTest
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Full integration test - loads entire Spring context
 * 
 * EXAMPLE:
 * ---------
 *     @SpringBootTest
 *     class ApplicationTests {
 *         
 *         @Autowired
 *         private UserService userService;
 *         
 *         @Test
 *         void contextLoads() {
 *             assertNotNull(userService);
 *         }
 *         
 *         @Test
 *         void shouldCreateUser() {
 *             User user = userService.create("John", "john@example.com");
 *             assertNotNull(user.getId());
 *         }
 *     }
 *     
 *     // With web environment
 *     @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
 *     class WebIntegrationTests {
 *         
 *         @Autowired
 *         private TestRestTemplate restTemplate;
 *         
 *         @Test
 *         void shouldReturnUsers() {
 *             ResponseEntity<List> response = 
 *                 restTemplate.getForEntity("/api/users", List.class);
 *             assertEquals(HttpStatus.OK, response.getStatusCode());
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @WebMvcTest
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Test only web layer (controller) - faster than full context
 * 
 * EXAMPLE:
 * ---------
 *     @WebMvcTest(UserController.class)
 *     class UserControllerTests {
 *         
 *         @Autowired
 *         private MockMvc mockMvc;
 *         
 *         @MockBean  // Mock the service
 *         private UserService userService;
 *         
 *         @Test
 *         void shouldReturnUsers() throws Exception {
 *             when(userService.findAll())
 *                 .thenReturn(List.of(new User("John")));
 *             
 *             mockMvc.perform(get("/api/users"))
 *                 .andExpect(status().isOk())
 *                 .andExpect(jsonPath("$[0].name").value("John"));
 *         }
 *         
 *         @Test
 *         void shouldCreateUser() throws Exception {
 *             User user = new User("John");
 *             when(userService.save(any())).thenReturn(user);
 *             
 *             mockMvc.perform(post("/api/users")
 *                     .contentType(MediaType.APPLICATION_JSON)
 *                     .content("{\"name\":\"John\"}"))
 *                 .andExpect(status().isCreated());
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @DataJpaTest
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Test only JPA/repository layer - uses in-memory database
 * 
 * EXAMPLE:
 * ---------
 *     @DataJpaTest
 *     class UserRepositoryTests {
 *         
 *         @Autowired
 *         private TestEntityManager entityManager;
 *         
 *         @Autowired
 *         private UserRepository userRepository;
 *         
 *         @Test
 *         void shouldFindByEmail() {
 *             User user = new User("john@example.com");
 *             entityManager.persist(user);
 *             entityManager.flush();
 *             
 *             Optional<User> found = userRepository.findByEmail("john@example.com");
 *             
 *             assertTrue(found.isPresent());
 *             assertEquals("john@example.com", found.get().getEmail());
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @MockBean and @SpyBean
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: 
 *   @MockBean → Complete mock (all methods return null/defaults)
 *   @SpyBean  → Partial mock (real methods, can stub specific ones)
 * 
 * EXAMPLE:
 * ---------
 *     @SpringBootTest
 *     class ServiceTests {
 *         
 *         @MockBean  // Complete mock
 *         private ExternalApiClient apiClient;
 *         
 *         @SpyBean   // Real bean, but can stub methods
 *         private UserService userService;
 *         
 *         @Autowired
 *         private OrderService orderService;
 *         
 *         @Test
 *         void testWithMock() {
 *             when(apiClient.fetchData()).thenReturn("mocked data");
 *             // apiClient is fully mocked
 *         }
 *         
 *         @Test
 *         void testWithSpy() {
 *             doReturn("stubbed").when(userService).getName();
 *             // Other userService methods work normally
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * OTHER TEST ANNOTATIONS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     // Test with specific profile
 *     @SpringBootTest
 *     @ActiveProfiles("test")
 *     class ProfileTests { }
 *     
 *     // Test with custom properties
 *     @SpringBootTest
 *     @TestPropertySource(properties = {"app.feature=true"})
 *     class PropertyTests { }
 *     
 *     // Test with property file
 *     @SpringBootTest
 *     @TestPropertySource(locations = "classpath:test.properties")
 *     class FilePropertyTests { }
 * 
 * TEST SLICE ANNOTATIONS SUMMARY:
 * --------------------------------
 * | Annotation    | Tests                  | Loads              |
 * |---------------|------------------------|---------------------|
 * | @SpringBootTest| Full integration      | Entire context      |
 * | @WebMvcTest   | Controller layer       | Web layer only      |
 * | @DataJpaTest  | Repository layer       | JPA + H2            |
 * | @JsonTest     | JSON serialization     | Jackson only        |
 * | @RestClientTest| REST clients          | RestTemplate only   |
 */
