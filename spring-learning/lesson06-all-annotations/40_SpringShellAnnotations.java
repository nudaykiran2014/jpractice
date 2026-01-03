package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 40: SPRING SHELL ANNOTATIONS (CLI Applications)                        ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 * 
 * Build interactive command-line applications with Spring.
 * 
 * Requires: spring-shell-starter
 */
public class _40_SpringShellAnnotations {
    public static void main(String[] args) {
        System.out.println("=== SPRING SHELL ANNOTATIONS ===\n");
        System.out.println("@ShellComponent     → Define shell command class");
        System.out.println("@ShellMethod        → Define a command");
        System.out.println("@ShellOption        → Command parameter");
        System.out.println("@ShellMethodAvailability → Conditional availability");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * @ShellComponent and @ShellMethod
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @ShellComponent
 *     public class MyCommands {
 *         
 *         @ShellMethod("Says hello")
 *         public String hello() {
 *             return "Hello, World!";
 *         }
 *         
 *         @ShellMethod("Adds two numbers")
 *         public int add(int a, int b) {
 *             return a + b;
 *         }
 *         
 *         @ShellMethod(value = "Greets a person", key = "greet")
 *         public String greet(@ShellOption(defaultValue = "World") String name) {
 *             return "Hello, " + name + "!";
 *         }
 *     }
 *     
 *     // Usage in shell:
 *     // shell:> hello
 *     // Hello, World!
 *     // shell:> add 5 3
 *     // 8
 *     // shell:> greet --name John
 *     // Hello, John!
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @ShellOption
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @ShellComponent
 *     public class FileCommands {
 *         
 *         @ShellMethod("Copy a file")
 *         public String copy(
 *             @ShellOption(value = {"-s", "--source"}) String source,
 *             @ShellOption(value = {"-d", "--dest"}) String destination,
 *             @ShellOption(defaultValue = "false") boolean overwrite
 *         ) {
 *             // copy --source file.txt --dest backup.txt --overwrite true
 *             return "Copied " + source + " to " + destination;
 *         }
 *         
 *         @ShellMethod("Process multiple files")
 *         public String process(
 *             @ShellOption(arity = 3) String[] files  // Exactly 3 files
 *         ) {
 *             return "Processing " + files.length + " files";
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * @ShellMethodAvailability
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Enable/disable commands based on state
 * 
 * EXAMPLE:
 * ---------
 *     @ShellComponent
 *     public class SecureCommands {
 *         
 *         private boolean loggedIn = false;
 *         
 *         @ShellMethod("Login to system")
 *         public String login(String username, String password) {
 *             loggedIn = true;
 *             return "Logged in as " + username;
 *         }
 *         
 *         @ShellMethod("View secret data")
 *         public String secret() {
 *             return "Secret: 42";
 *         }
 *         
 *         // This method controls availability of 'secret' command
 *         @ShellMethodAvailability("secret")
 *         public Availability secretAvailability() {
 *             return loggedIn 
 *                 ? Availability.available()
 *                 : Availability.unavailable("You must login first");
 *         }
 *     }
 * 
 * ═══════════════════════════════════════════════════════════════════════════════
 * GROUPED COMMANDS
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * EXAMPLE:
 * ---------
 *     @ShellComponent
 *     @ShellCommandGroup("User Management")
 *     public class UserCommands {
 *         
 *         @ShellMethod("Create user")
 *         public String createUser(String name) { return "Created: " + name; }
 *         
 *         @ShellMethod("Delete user")
 *         public String deleteUser(String name) { return "Deleted: " + name; }
 *     }
 *     
 *     @ShellComponent
 *     @ShellCommandGroup("Database")
 *     public class DbCommands {
 *         
 *         @ShellMethod("Run migration")
 *         public String migrate() { return "Migrated"; }
 *     }
 */
