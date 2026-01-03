package spring_learning.lesson06_all_annotations;

/**
 * ╔══════════════════════════════════════════════════════════════════════════════╗
 * ║  PART 8: EXCEPTION HANDLING ANNOTATIONS                                      ║
 * ╚══════════════════════════════════════════════════════════════════════════════╝
 */
public class _08_ExceptionHandlingAnnotations {
    public static void main(String[] args) {
        System.out.println("=== EXCEPTION HANDLING ANNOTATIONS ===\n");
        System.out.println("@ControllerAdvice     → Global exception handler (MVC)");
        System.out.println("@RestControllerAdvice → Global exception handler (REST)");
        System.out.println("@ExceptionHandler     → Handle specific exception");
    }
}

/*
 * ═══════════════════════════════════════════════════════════════════════════════
 * @RestControllerAdvice / @ControllerAdvice
 * ═══════════════════════════════════════════════════════════════════════════════
 * 
 * PURPOSE: Global exception handling for all controllers
 *          @RestControllerAdvice = @ControllerAdvice + @ResponseBody
 * 
 * WHERE: Separate exception handler class
 * 
 * EXAMPLE:
 * ---------
 *     @RestControllerAdvice
 *     public class GlobalExceptionHandler {
 *         
 *         // Handle specific exception
 *         @ExceptionHandler(ResourceNotFoundException.class)
 *         @ResponseStatus(HttpStatus.NOT_FOUND)
 *         public ErrorResponse handleNotFound(ResourceNotFoundException ex) {
 *             return new ErrorResponse("NOT_FOUND", ex.getMessage());
 *         }
 *         
 *         // Handle validation errors
 *         @ExceptionHandler(MethodArgumentNotValidException.class)
 *         @ResponseStatus(HttpStatus.BAD_REQUEST)
 *         public ErrorResponse handleValidation(MethodArgumentNotValidException ex) {
 *             List<String> errors = ex.getBindingResult()
 *                 .getFieldErrors()
 *                 .stream()
 *                 .map(e -> e.getField() + ": " + e.getDefaultMessage())
 *                 .collect(Collectors.toList());
 *             return new ErrorResponse("VALIDATION_ERROR", errors);
 *         }
 *         
 *         // Handle multiple exception types
 *         @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
 *         @ResponseStatus(HttpStatus.BAD_REQUEST)
 *         public ErrorResponse handleBadRequest(RuntimeException ex) {
 *             return new ErrorResponse("BAD_REQUEST", ex.getMessage());
 *         }
 *         
 *         // Catch-all for unhandled exceptions
 *         @ExceptionHandler(Exception.class)
 *         @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
 *         public ErrorResponse handleAll(Exception ex) {
 *             return new ErrorResponse("INTERNAL_ERROR", "Unexpected error");
 *         }
 *     }
 *     
 *     // Error response DTO
 *     public class ErrorResponse {
 *         private String code;
 *         private Object message;
 *         private LocalDateTime timestamp = LocalDateTime.now();
 *         
 *         public ErrorResponse(String code, Object message) {
 *             this.code = code;
 *             this.message = message;
 *         }
 *         // getters
 *     }
 *     
 *     // Custom exception with built-in status
 *     @ResponseStatus(HttpStatus.NOT_FOUND)
 *     public class ResourceNotFoundException extends RuntimeException {
 *         public ResourceNotFoundException(String message) {
 *             super(message);
 *         }
 *     }
 *     
 *     // Usage in service:
 *     @Service
 *     public class UserService {
 *         public User findById(Long id) {
 *             return userRepository.findById(id)
 *                 .orElseThrow(() -> new ResourceNotFoundException(
 *                     "User not found: " + id));
 *         }
 *     }
 */
