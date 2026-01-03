package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 28: MAPSTRUCT ANNOTATIONS (Object Mapping)                             ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * MapStruct generates type-safe mapping code at compile time.
 * Convert between entities, DTOs, and other objects automatically.
 * 
 * Requires: mapstruct + mapstruct-processor
 */
public class _28_MapStructAnnotations {
    public static void main(String[] args) {
        System.out.println("=== MAPSTRUCT ANNOTATIONS ===\n");
        System.out.println("@Mapper          → Define a mapper interface");
        System.out.println("@Mapping         → Customize field mapping");
        System.out.println("@Mappings        → Multiple @Mapping");
        System.out.println("@InheritConfiguration → Reuse mapping config");
        System.out.println("@AfterMapping    → Post-processing hook");
        System.out.println("@BeforeMapping   → Pre-processing hook");
        System.out.println("@MappingTarget   → Update existing object");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Mapper
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Define a mapper interface - MapStruct generates the implementation
 * 
 * EXAMPLE:
 * ---------
 *     // Entity
 *     public class User {
 *         private Long id;
 *         private String firstName;
 *         private String lastName;
 *         private String email;
 *         private LocalDateTime createdAt;
 *     }
 *     
 *     // DTO
 *     public class UserDTO {
 *         private Long id;
 *         private String firstName;
 *         private String lastName;
 *         private String email;
 *     }
 *     
 *     // Mapper interface
 *     @Mapper(componentModel = "spring")  // Creates Spring bean
 *     public interface UserMapper {
 *         
 *         UserDTO toDto(User user);
 *         
 *         User toEntity(UserDTO dto);
 *         
 *         List<UserDTO> toDtoList(List<User> users);
 *     }
 *     
 *     // Usage in service
 *     @Service
 *     public class UserService {
 *         
 *         @Autowired
 *         private UserMapper userMapper;
 *         
 *         public UserDTO getUser(Long id) {
 *             User user = userRepository.findById(id).orElseThrow();
 *             return userMapper.toDto(user);
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @Mapping (Field Customization)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Customize how specific fields are mapped
 * 
 * EXAMPLE:
 * ---------
 *     @Mapper(componentModel = "spring")
 *     public interface UserMapper {
 *         
 *         // Different field names
 *         @Mapping(source = "firstName", target = "name")
 *         @Mapping(source = "emailAddress", target = "email")
 *         UserDTO toDto(User user);
 *         
 *         // Ignore a field
 *         @Mapping(target = "password", ignore = true)
 *         UserDTO toDtoSafe(User user);
 *         
 *         // Constant value
 *         @Mapping(target = "status", constant = "ACTIVE")
 *         UserDTO toNewUserDto(User user);
 *         
 *         // Expression (Java code)
 *         @Mapping(target = "fullName", expression = "java(user.getFirstName() + \" \" + user.getLastName())")
 *         UserDTO toDtoWithFullName(User user);
 *         
 *         // Date format
 *         @Mapping(source = "createdAt", target = "createdDate", dateFormat = "yyyy-MM-dd")
 *         UserDTO toDtoFormatted(User user);
 *         
 *         // Default value
 *         @Mapping(source = "nickname", target = "displayName", defaultValue = "Anonymous")
 *         UserDTO toDtoWithDefault(User user);
 *         
 *         // Nested object field
 *         @Mapping(source = "address.city", target = "city")
 *         @Mapping(source = "address.country", target = "country")
 *         UserDTO toDtoFlattened(User user);
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @MappingTarget (Update Existing Object)
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Update an existing object instead of creating new one
 * 
 * EXAMPLE:
 * ---------
 *     @Mapper(componentModel = "spring")
 *     public interface UserMapper {
 *         
 *         // Update existing entity from DTO
 *         void updateUserFromDto(UserDTO dto, @MappingTarget User user);
 *         
 *         // With customization
 *         @Mapping(target = "id", ignore = true)  // Don't overwrite ID
 *         @Mapping(target = "createdAt", ignore = true)  // Don't overwrite
 *         void updateUser(UserDTO dto, @MappingTarget User user);
 *     }
 *     
 *     // Usage
 *     @Service
 *     public class UserService {
 *         
 *         public User updateUser(Long id, UserDTO dto) {
 *             User user = userRepository.findById(id).orElseThrow();
 *             userMapper.updateUser(dto, user);  // Updates existing user
 *             return userRepository.save(user);
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @AfterMapping and @BeforeMapping
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Custom logic before/after mapping
 * 
 * EXAMPLE:
 * ---------
 *     @Mapper(componentModel = "spring")
 *     public abstract class UserMapper {
 *         
 *         public abstract UserDTO toDto(User user);
 *         
 *         @AfterMapping
 *         protected void enrichDto(User user, @MappingTarget UserDTO dto) {
 *             // Custom logic after mapping
 *             dto.setFullName(user.getFirstName() + " " + user.getLastName());
 *             dto.setAgeGroup(calculateAgeGroup(user.getBirthDate()));
 *         }
 *         
 *         @BeforeMapping
 *         protected void validate(User user) {
 *             if (user == null) {
 *                 throw new IllegalArgumentException("User cannot be null");
 *             }
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * Using Other Mappers
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Compose mappers for nested objects
 * 
 * EXAMPLE:
 * ---------
 *     @Mapper(componentModel = "spring")
 *     public interface AddressMapper {
 *         AddressDTO toDto(Address address);
 *     }
 *     
 *     @Mapper(componentModel = "spring", uses = {AddressMapper.class})
 *     public interface UserMapper {
 *         
 *         // AddressMapper will be used automatically for address field
 *         UserDTO toDto(User user);
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * Null Value Handling
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @Mapper(
 *         componentModel = "spring",
 *         nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
 *         nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
 *     )
 *     public interface UserMapper {
 *         
 *         // NULL values in source won't overwrite target
 *         void updateUser(UserDTO dto, @MappingTarget User user);
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * Enum Mapping
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     public enum UserStatus { ACTIVE, INACTIVE, PENDING }
 *     public enum StatusDTO { A, I, P }
 *     
 *     @Mapper(componentModel = "spring")
 *     public interface StatusMapper {
 *         
 *         @ValueMapping(source = "ACTIVE", target = "A")
 *         @ValueMapping(source = "INACTIVE", target = "I")
 *         @ValueMapping(source = "PENDING", target = "P")
 *         StatusDTO toDto(UserStatus status);
 *     }
 * 
 * DEPENDENCY:
 * ------------
 *     <dependency>
 *         <groupId>org.mapstruct</groupId>
 *         <artifactId>mapstruct</artifactId>
 *         <version>1.5.5.Final</version>
 *     </dependency>
 *     
 *     <!-- Annotation processor -->
 *     <plugin>
 *         <artifactId>maven-compiler-plugin</artifactId>
 *         <configuration>
 *             <annotationProcessorPaths>
 *                 <path>
 *                     <groupId>org.mapstruct</groupId>
 *                     <artifactId>mapstruct-processor</artifactId>
 *                     <version>1.5.5.Final</version>
 *                 </path>
 *             </annotationProcessorPaths>
 *         </configuration>
 *     </plugin>
 */
