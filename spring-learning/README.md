# Spring & Spring Boot Learning Path 🚀

A hands-on journey from Java basics to Spring Boot mastery!

## How to Use This Course

Run each lesson in order. Each builds on the previous one.

## Lessons Overview

### Lesson 1: POJO (Plain Old Java Object)
**Folder:** `lesson01-pojo/`

Learn what POJOs are - the foundation of Spring!
- Simple classes with fields, getters, setters
- No framework dependencies
- Spring is built around POJOs

```bash
# Run from jpractice folder
javac -d out spring-learning/lesson01-pojo/*.java && java -cp out spring_learning.lesson01_pojo.PojoDemo
```

---

### Lesson 2: Why Spring Was Created
**Folder:** `lesson02-why-spring/`

The story of Java enterprise development:
- 1995-2000: Plain Java
- 2000-2004: EJB Hell (complex, painful)
- 2004: Spring Framework (POJOs + DI)
- 2014: Spring Boot (auto-config magic)

```bash
javac -d out spring-learning/lesson02-why-spring/*.java && java -cp out spring_learning.lesson02_why_spring.WhySpringWasCreated
```

---

### Lesson 3: Dependency Injection
**Folder:** `lesson03-dependency-injection/`

**THE MOST IMPORTANT CONCEPT!**

Learn how Spring "injects" dependencies:
- Problem: Tight coupling (bad)
- Solution: Program to interfaces + inject dependencies
- Benefits: Loose coupling, testability, flexibility

```bash
javac -d out spring-learning/lesson03-dependency-injection/solution/*.java spring-learning/lesson03-dependency-injection/*.java && java -cp out spring_learning.lesson03_dependency_injection.DependencyInjectionDemo
```

---

### Lesson 4: IoC Container
**Folder:** `lesson04-ioc-container/`

Understand Spring's "brain":
- IoC = Inversion of Control (framework creates objects)
- Container = The "warehouse" that holds beans
- Singleton = One instance shared (default)

```bash
javac -d out spring-learning/lesson04-ioc-container/*.java && java -cp out spring_learning.lesson04_ioc_container.IoCContainerDemo
```

---

### Lesson 5: Spring Boot
**Folder:** `lesson05-spring-boot/`

The modern way to build Spring apps:
- Auto-configuration (smart defaults)
- Embedded server (just run!)
- Annotations cheat sheet

```bash
javac -d out spring-learning/lesson05-spring-boot/*.java && java -cp out spring_learning.lesson05_spring_boot.SpringBootMagicDemo

# Also run the cheat sheet:
java -cp out spring_learning.lesson05_spring_boot.AnnotationsCheatSheet
```

---

## Key Concepts Summary

| Term | Meaning |
|------|---------|
| **POJO** | Plain Old Java Object - simple class |
| **Bean** | Object managed by Spring |
| **IoC** | Inversion of Control - Spring creates objects |
| **DI** | Dependency Injection - Spring wires objects |
| **Container** | Spring's "warehouse" holding beans |
| **@Component** | "This class is a bean" |
| **@Service** | "This is a business logic bean" |
| **@Repository** | "This is a data access bean" |
| **@Autowired** | "Inject a bean here" |
| **@RestController** | "This handles REST requests" |

---

## Next Steps

1. ✅ Complete all 5 lessons
2. 🌐 Go to [start.spring.io](https://start.spring.io)
3. 📦 Create a project with Web, JPA dependencies
4. 🏃 Build your first REST API!

---

## Recommended Learning Path After This

1. Build a simple CRUD REST API
2. Add database (H2 → PostgreSQL)
3. Add validation (@Valid, @NotNull)
4. Add error handling (@ControllerAdvice)
5. Add security (Spring Security)
6. Add tests (@SpringBootTest)

Happy Learning! 🎉
